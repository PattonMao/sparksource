package com.mj.sparksource.week2.event

import com.typesafe.config.ConfigFactory
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import com.mj.sparksource.week2.actor.ClientAcotr

class EventProcessLoop(name: String) extends ProcessLoop[Event](name) {
  val _system = ActorSystem("Client", ConfigFactory.load().getConfig("Client"))
  val clientActor: ActorRef = _system.actorOf(
    Props[ClientAcotr], name = "client")
  /**
   * Invoked in the event thread when polling events from the event queue.
   *
   * Note: Should avoid calling blocking actions in `onReceive`, or the event thread will be blocked
   * and cannot process events in time. If you want to call some blocking actions, run them in
   * another thread.
   */

  override protected def onPostEvent(event: Event): Unit = {
    clientActor ! event
  }

  /**
   * Invoked if `onReceive` throws any non fatal error. Any non fatal error thrown from `onError`
   * will be ignored.
   */
  override protected def onDropEvent(event: Event): Unit = {
    println(" no remaining room in event queue.")
  }
}

