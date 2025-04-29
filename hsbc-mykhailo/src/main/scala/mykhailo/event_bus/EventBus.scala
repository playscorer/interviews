package mykhailo.event_bus

import java.util.concurrent.{ConcurrentHashMap, ConcurrentLinkedQueue}
import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

case class EventKey[T](value: String) extends AnyVal

trait Subscriber[T] {
  def onEvent(event: T): Unit
}

object EventBus {
  def compactGetLatest[T](fst: T, snd: T): T = snd
}

/**
 * Do not pass [[scala.concurrent.ExecutionContext.parasitic]] or something similar as a parameter in production cpde.
 * It will cause subscriber's event callback execution on publisher's thread and
 * delaying events propagation to other subscribers.
 */
trait EventBus {

  def publishEvent[T](key: EventKey[T])(event: T): Unit

  def subscribeTo[T](key: EventKey[T])
                    (subscriber: Subscriber[T])
                    (implicit ec: ExecutionContext): Unit

  def subscribeToFiltered[T](key: EventKey[T])
                    (subscriber: Subscriber[T], filter: T => Boolean)
                    (implicit ec: ExecutionContext): Unit

  /**
   * @param compact Function for compacting multiple events into one.
   *                Default behavior is to pick the newest event.
   */
  def subscribeToCompacted[T](key: EventKey[T])
                    (subscriber: Subscriber[T], compact: (T, T) => T = EventBus.compactGetLatest[T](_,_))
                    (implicit ec: ExecutionContext): Unit
}

object ConcurrentEventBus {

  class SubscriberNode[T](subscriber: Subscriber[T], key: EventKey[T], ec: ExecutionContext) extends StrictLogging {
    protected val queue: ConcurrentLinkedQueue[T] = new ConcurrentLinkedQueue[T]()

    protected def getNext: Option[T] =
      Option(queue.poll())

    def HandleEvent(event: T): Unit = {
      queue.add(event)
      ec.execute { () =>
        getNext match {
          case Some(event) =>
            logger.debug(s"Invoking subscriber $subscriber for '${key.value}' event: $event")
            subscriber.onEvent(event)
          case None =>
            logger.debug(s"No '${key.value}' events available for the subscriber $subscriber")
        }
      }
    }
  }

  class SubscriberFilterNode[T](subscriber: Subscriber[T], key: EventKey[T], ec: ExecutionContext, filter: T => Boolean)
    extends SubscriberNode(subscriber, key, ec) {

    @tailrec
    override final protected def getNext: Option[T] = {
      val next = Option(queue.poll())
      if (next.forall(filter)) next
      else getNext
    }
  }

  class SubscriberCompactNode[T](subscriber: Subscriber[T], key: EventKey[T], ec: ExecutionContext, compact: (T, T) => T)
    extends SubscriberNode(subscriber, key, ec) {

    override protected def getNext: Option[T] = {
      val events = LazyList.continually(queue.poll()).takeWhile(_ != null)
      logger.debug(s"Compacting ${events.size} ${key.value} events.")
      events.reduceLeftOption(compact)
    }
  }
}

/**
 * Implementation of a thread safe EventBus.
 * Main design goals: high throughput and ease of implementation.
 * Each subscriber has a dedicated queue, so stable order guaranty
 * across different subscribers is NOT provided.
 */
class ConcurrentEventBus() extends EventBus with StrictLogging {
  import ConcurrentEventBus._

  private val nodesByKey = new ConcurrentHashMap[EventKey[_], Set[SubscriberNode[_]]]()

  private def addNode[T](key: EventKey[T], node: SubscriberNode[T]): Unit = {
    nodesByKey.compute(key, (_, current) => { // atomic operation
      if (current == null) Set(node)
      else current + node
    })
  }

  override def publishEvent[T](key: EventKey[T])(event: T): Unit = {
    nodesByKey.getOrDefault(key, Set.empty).foreach(_.asInstanceOf[SubscriberNode[T]].handleEvent(event))
  }

  override def subscribeTo[T](key: EventKey[T])
                             (subscriber: Subscriber[T])
                             (implicit ec: ExecutionContext): Unit = {
    logger.info(s"Adding subscriber $subscriber for ${key.value} events.")
    addNode(key, new SubscriberNode(subscriber, key, ec))
  }

  override def subscribeToFiltered[T](key: EventKey[T])
                                     (subscriber: Subscriber[T], filter: T => Boolean)
                                     (implicit ec: ExecutionContext): Unit = {
    logger.info(s"Adding subscriber $subscriber for ${key.value} events with filter.")
    addNode(key, new SubscriberFilterNode()(subscriber, key, ec, filter))
  }

  override def subscribeToCompacted[T](key: EventKey[T])
                                      (subscriber: Subscriber[T], compact: (T, T) => T)
                                      (implicit ec: ExecutionContext): Unit = {
    logger.info(s"Adding subscriber $subscriber for ${key.value} events with compaction.")
    addNode(key, new SubscriberCompactNode()()(subscriber, key, ec, compact))
  }
}
