package citi.prep2026;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Key design decisions to remember:
 *
 * ConcurrentHashMap — main cache, thread-safe bin-level locking
 * ConcurrentSkipListMap — sorted expiry index, headMap(now) efficiently finds all expired buckets
 * ConcurrentLinkedQueue per timestamp bucket — lock-free O(1) insert, simple drain
 * cache.compute() in getOrCompute — atomic check-and-compute, exactly once per key
 * cache.remove(key, entry) — conditional remove, won't delete a freshly inserted entry
 * Cleaner thread is daemon — JVM can exit without waiting for it
 * scheduleAtFixedRate — cleaner runs every second regardless of how long cleanup takes
 */
public class ExpiringCache<K, V> {

    record CacheEntry<V>(V value, long expiryTime) {
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final ConcurrentSkipListMap<Long, ConcurrentLinkedQueue<K>> expiryIndex = new ConcurrentSkipListMap<>();
    private final long ttlMs;
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(
        r -> {
            Thread t = new Thread(r);
            t.setName("cache-cleaner");
            t.setDaemon(true);
            return t;
        }
    );

    public ExpiringCache(long ttlMs) {
        this.ttlMs = ttlMs;
        // run cleaner every second
        cleaner.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.SECONDS);
    }

    // put — add entry to cache and expiry index
    public void put(K key, V value) {
        long expiryTime = System.currentTimeMillis() + ttlMs;
        CacheEntry<V> entry = new CacheEntry<>(value, expiryTime);
        cache.put(key, entry);

        // add key to expiry index bucket
        expiryIndex
            .computeIfAbsent(expiryTime, k -> new ConcurrentLinkedQueue<>())
            .add(key);
    }

    // get — return value if present and not expired
    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            return null;
        }
        return entry.value();
    }

    // getOrCompute — atomic check-compute-insert
    public V getOrCompute(K key, java.util.function.Function<K, V> mappingFunction) {
        // compute() is atomic at bin level — exactly once per key
        CacheEntry<V> entry = cache.compute(key, (k, existing) -> {
            if (existing != null && !existing.isExpired()) {
                return existing; // still valid — keep it
            }
            // absent or expired — compute fresh value
            V computed = mappingFunction.apply(k);
            long expiryTime = System.currentTimeMillis() + ttlMs;

            // register in expiry index
            expiryIndex
                .computeIfAbsent(expiryTime, ek -> new ConcurrentLinkedQueue<>())
                .add(k);

            return new CacheEntry<>(computed, expiryTime);
        });
        return entry.value();
    }

    // cleaner — runs every second
    private void cleanup() {
        long now = System.currentTimeMillis();

        // headMap returns all entries with timestamp <= now
        expiryIndex.headMap(now, true).forEach((expiryTime, keys) -> {
            keys.forEach(key -> {
                // conditional remove — only remove if entry hasn't been refreshed
                CacheEntry<V> entry = cache.get(key);
                if (entry != null && entry.isExpired()) {
                    cache.remove(key, entry); // atomic — won't remove fresh entry
                }
            });
            expiryIndex.remove(expiryTime); // clean up index bucket
        });
    }

    public void shutdown() {
        cleaner.shutdown();
    }

    // test
    public static void main(String[] args) throws InterruptedException {
        ExpiringCache<String, Double> cache = new ExpiringCache<>(5000); // 5s TTL

        // put some prices
        cache.put("EURUSD", 1.0850);
        cache.put("GBPUSD", 1.2700);

        // get immediately — should return values
        System.out.println("EURUSD: " + cache.get("EURUSD")); // 1.085
        System.out.println("GBPUSD: " + cache.get("GBPUSD")); // 1.27

        // getOrCompute — should use cached value
        Double price = cache.getOrCompute("EURUSD", k -> {
            System.out.println("Computing " + k); // should NOT print
            return 1.0900;
        });
        System.out.println("EURUSD computed: " + price); // 1.085

        // wait for expiry
        Thread.sleep(6000);

        // get after expiry — should return null
        System.out.println("EURUSD after expiry: " + cache.get("EURUSD")); // null

        // getOrCompute after expiry — should recompute
        Double recomputed = cache.getOrCompute("EURUSD", k -> {
            System.out.println("Recomputing " + k); // should print
            return 1.0900;
        });
        System.out.println("EURUSD recomputed: " + recomputed); // 1.09

        cache.shutdown();
    }
}