package com.mj.sparksource.week2.actor

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import com.mj.sparksource.week2.event.Learning

class LearnActor extends Actor {
  val ip = ConfigFactory.load().getConfig("Server").getString("akka.remote.netty.tcp.hostname")
  val name = context.getClass.getName
  override def receive: Receive = {
    case event: Learning =>
      sender ! ip + " LearnActor: I am learning"
      println("LearnActor: I am learning")
  }
}