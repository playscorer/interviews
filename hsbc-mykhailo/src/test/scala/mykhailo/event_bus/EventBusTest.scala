package mykhailo.event_bus

import java.util.concurrent.{ConcurrentLinkedDeque, ConcurrentLinkedQueue, Executors, TimeUnit}
import scala.Console.in
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters.CollectionHasAsScala

/*
  Of course this set of tests is not enough to guarantee correctness of concurrent node.
  In reality we would use something like tempus-figit, MultithreadedTC, jcstress, etc.
  But I assume it is out of scope for this test assessment.
 */

class EventBusTest extends AnyFlatSpec with Matchers {
  val EventA = EventKey[Int]("A")
  val EventB = EventKey[Int]("B")

  val workerThreadName1 = "EventBusWorker1"
  val workerThreadName2 = "EventBusWorker2"

  it should "allow subscribing, publishing and receiving events" in {
    val eventBus = new ConcurrentEventBus()

    val subscriber1Events = new ConcurrentLinkedQueue[Int]()

    eventBus.subscribeTo(EventA)((event: Int) => {
      subscriber1Events.add(event)
    })(ExecutionContext.parasitic)

    eventBus.publishEvent(EventA)(1)

    subscriber1Events.asScala.toList shouldBe List(1)
  }

  it should "allow single subscriber to subscribe for multiple events" in {
    val eventBus = new ConcurrentEventBus()

    val subscriber1Events = new ConcurrentLinkedQueue[Int]()

    eventBus.subscribeTo(EventA)((event: Int) => {
      subscriber1Events.add(event)
    })(ExecutionContext.parasitic)

    eventBus.subscribeTo(EventB)((event: Int) => {
      subscriber1Events.add(event)
    })(ExecutionContext.parasitic)

    eventBus.publishEvent(EventA)(1)
    eventBus.publishEvent(EventB)(2)

    subscriber1Events.asScala.toList shouldBe Set(1, 2)
  }

  it should "distribute a single event to multiple subscribers" in {
    val eventBus = new ConcurrentEventBus()

    val subscriber1Events = new ConcurrentLinkedQueue[Int]()
    val subscriber2Events = new ConcurrentLinkedQueue[Int]()

    eventBus.subscribeTo(EventA)((event: Int) => {
      subscriber1Events.add(event)
    })(ExecutionContext.parasitic)

    eventBus.subscribeTo(EventA)((event: Int) => {
      subscriber1Events.add(event)
    })(ExecutionContext.parasitic)

    eventBus.publishEvent(EventA)(1)

    subscriber1Events.asScala.toList shouldBe List(1)
    subscriber2Events.asScala.toList shouldBe List(1)
  }

  it should "filter events for filtered subscriptions" in {
    val eventBus = new ConcurrentEventBus()

    val subscriber1Events = new ConcurrentLinkedQueue[Int]()
    val subscriber2Events = new ConcurrentLinkedQueue[Int]()

    eventBus.subscribeToFiltered(EventA)((event: Int) => {
      subscriber1Events.add(event)
    }, _ % 2 == 0)(ExecutionContext.parasitic)

    eventBus.subscribeToFiltered(EventA)((event: Int) => {
      subscriber2Events.add(event)
    }, _ % 2 == 0)(ExecutionContext.parasitic)

    eventBus.publishEvent(EventA)(1)
    eventBus.publishEvent(EventA)(2)
    eventBus.publishEvent(EventA)(3)
    eventBus.publishEvent(EventA)(4)

    subscriber1Events.asScala.toList shouldBe List(2, 4)
    subscriber2Events.asScala.toList shouldBe List(1, 3)
  }

  it should "execute event handling using provided ExecutionContext" in {
    val eventBus = new ConcurrentEventBus()

    val ec1 = ExecutionContext.fromExecutorService(
      Executors.newSingleThreadExecutor((r: Runnable) => {
        val t = new Thread(r)
        t.setName(workerThreadName1)
        t
      })
    )

    val usedThreads = new ConcurrentLinkedQueue[String]()

    eventBus.subscribeTo(EventA)((event: Int) => {
      usedThreads.add(Thread.currentThread().getName)
    })(ec1)

    eventBus.publishEvent(EventA)(1)

    TimeUnit.SECONDS.sleep(1)
    usedThreads.asScala.toSet shouldBe Set(workerThreadName1)
  }

  it should "not loose events when events are published from different threads" in {
    val eventBus = new ConcurrentEventBus()

    val subscriber1Events = new ConcurrentLinkedQueue[Int]()
    val subscriber2Events = new ConcurrentLinkedQueue[Int]()

    eventBus.subscribeTo(EventA)((event: Int) => {
      subscriber1Events.add(event)
    })(ExecutionContext.global)

    eventBus.subscribeTo(EventA)((event: Int) => {
      subscriber2Events.add(event)
    })(ExecutionContext.global)

    ExecutionContext.global.execute(() => (0 until 1000)
      .foreach(eventBus.publishEvent(EventA)(_)))

    ExecutionContext.global.execute(() => (0 until 2000)
      .foreach(eventBus.publishEvent(EventA)(_)))

    TimeUnit.SECONDS.sleep(2)
    subscriber1Events.asScala.toSet shouldBe subscriber2Events.asScala.toSet
    subscriber2Events.asScala.toSet shouldBe (0 until 2000).toSet
  }

  it should "not loose subscribers when subscribers subscribing from different threads" in {
    val eventBus = new ConcurrentEventBus()

    val allSubscribersEvents = new ConcurrentLinkedQueue[Int]()

    ExecutionContext.global.execute(() => (0 until 1000).foreach {_ =>
      eventBus.subscribeTo(EventA)((event: Int) => {
        allSubscribersEvents.add(event)
      })(ExecutionContext.global)
    })

    ExecutionContext.global.execute(() => (0 until 1000).foreach { _ =>
      eventBus.subscribeTo(EventA)((event: Int) => {
        allSubscribersEvents.add(event)
      })(ExecutionContext.global)
    })

    TimeUnit.SECONDS.sleep(1)
    eventBus.publishEvent(EventA)(1)

    TimeUnit.SECONDS.sleep(1)
    allSubscribersEvents.asScala.size shouldBe 2000
  }

  it should "compact events for compacted subscriptions" in {
    val eventBus = new ConcurrentEventBus()

    val ec1 = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())

    val subscriber1Events = new ConcurrentLinkedQueue[Int]()

    eventBus.subscribeToCompacted(EventA)((event: Int) => {
      subscriber1Events.add(event)
      TimeUnit.SECONDS.sleep(1)
    })(ec1)

    eventBus.publishEvent(EventA)(1)
    TimeUnit.SECONDS.sleep(1)
    eventBus.publishEvent(EventA)(2)
    eventBus.publishEvent(EventA)(3)
    eventBus.publishEvent(EventA)(4)
    eventBus.publishEvent(EventA)(5)

    TimeUnit.SECONDS.sleep(2)
    subscriber1Events.asScala.toList shouldBe List(1, 5)
  }
}
