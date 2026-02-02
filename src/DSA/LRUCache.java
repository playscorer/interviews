package DSA;

import java.util.HashMap;
import java.util.Map;

class LRUCache {
    int capacity;
    Map<Integer, ListNode> cacheMap;
    ListNode head;
    ListNode tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        cacheMap = new HashMap<>(capacity);
        head = new ListNode();
        tail = new ListNode();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        System.out.println("get " + key);
        if (!cacheMap.containsKey(key)) {
            return -1;
        }
        ListNode node = cacheMap.get(key);
        removeNodeFromList(node);
        addToFront(node);

        /*
        ListNode l = head;
        while (l != null) {
            System.out.print(l + " ");
            l = l.next;
        }
        System.out.println();
        */

        return node.val;
    }

    public void put(int key, int value) {
        System.out.println("put");
        if (cacheMap.containsKey(key)) {
            ListNode node = cacheMap.get(key);
            removeNodeFromList(node);
        }

        ListNode node = new ListNode(key, value);
        cacheMap.put(key, node);
        addToFront(node);

        System.out.println(cacheMap.size() + "/" + capacity);
        if (cacheMap.size() > capacity) {
            node = tail.prev;
            cacheMap.remove(node.key);
            removeNodeFromList(node);
        }
        //System.out.println(cacheMap);

        /*
        ListNode l = head;
        while (l != null) {
            System.out.print(l + " ");
            l = l.next;
        }
        System.out.println();
        */

    }

    public void addToFront(ListNode node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    public void removeNodeFromList(ListNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

}

class ListNode {
    int key;
    int val;
    ListNode next;
    ListNode prev;

    public ListNode() {
    }

    public ListNode(int key, int val) {
        this.key = key;
        this.val = val;
    }

    public String toString() {
        return "["+key+" "+val+"]";
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
