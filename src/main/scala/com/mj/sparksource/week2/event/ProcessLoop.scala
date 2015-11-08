package com.mj.sparksource.week2.event

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{ BlockingQueue, LinkedBlockingDeque }
import scala.util.control.NonFatal
import java.util.concurrent.Semaphore
import java.util.concurrent.CopyOnWriteArrayList

/**
 *
 */
abstract class ProcessLoop[E](name: String) {
  self =>

  //max capacity 30000
  private val MAX_QUEUE_CAPACITY = 30000
  private val eventQueue: LinkedBlockingDeque[E] = new LinkedBlockingDeque[E](MAX_QUEUE_CAPACITY)
  // Indicate if `start()` is called
  private val started = new AtomicBoolean(false)
  // Indicate if `stop()` is called
  private val stopped = new AtomicBoolean(false)

  // Indicate if we are processing some event
  // Guarded by `self`
  private var processingEvent = false
  // A counter that represents the number of events produced and consumed in the queue
  private val eventLock = new Semaphore(0)

  private val listenerThread = new Thread(name) {
    setDaemon(true)

    override def run(): Unit = {
      try {
        while (true) {
          eventLock.acquire()
          self.synchronized {
            processingEvent = true
          }
          try {
            val event = eventQueue.poll
            if (event == null) {
              // Get out of the while loop and shutdown the daemon thread
              if (!stopped.get) {
                throw new IllegalStateException("Polling `null` from eventQueue means" +
                  " the listener bus has been stopped. So `stopped` must be true")
              }
              return
            }
            onPostEvent(event)
          } finally {
            self.synchronized {
              processingEvent = false
            }
          }
        }
      } catch {
        case ie: InterruptedException => // exit even if eventQueue is not empty
        case NonFatal(e) => println("Unexpected error in " + name, e)
      }
    }
  }

  def start(): Unit = {
    if (started.compareAndSet(false, true)) {
      listenerThread.start()
    } else {
      throw new IllegalArgumentException("queue already started!")
    }
  }

  def stop(): Unit = {
    if (!started.get()) {
      throw new IllegalStateException(s"Attempted to stop $name that has not yet started!")
    }
    if (stopped.compareAndSet(false, true)) {
      // Call eventLock.release() so that listenerThread will poll `null` from `eventQueue` and know
      // `stop` is called.
      eventLock.release()
      listenerThread.join()
    } else {
      // Keep quiet
    }
  }

  /**
   * Post an event to the specified listener. `onPostEvent` is guaranteed to be called in the same
   * thread.
   */
  protected def onPostEvent(event: E): Unit

  /**
   * Put the event into the event queue. The event thread will process it later.
   */
  def post(event: E): Unit = {
    if (stopped.get) {
      // Drop further events to make `listenerThread` exit ASAP
      println(s"$name has already stopped! Dropping event $event")
      return
    }
    val eventAdd = eventQueue.offer(event)
    if (eventAdd) {
      eventLock.release
    } else {
      onDropEvent(event)
    }
  }

  /**
   * Return if the event thread has already been started but not yet stopped.
   */
  def isActive: Boolean = listenerThread.isAlive

  /**
   * Invoked if the queue is full
   */
  protected def onDropEvent(event: E): Unit
}