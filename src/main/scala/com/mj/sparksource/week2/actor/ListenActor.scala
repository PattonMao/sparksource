package com.mj.sparksource.week2.actor

import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.actorRef2Scala
import com.mj.sparksource.week2.event.Listening

class ListenActor extends Actor {
  val ip = ConfigFactory.load().getConfig("Server").getString("akka.remote.netty.tcp.hostname")
  val name = context.getClass.getName
  override def receive: Receive = {
    case event: Listening =>
      sender ! ip + " ListenActor: I am listening"
      println("ListenActor: I am listening")
  }
}