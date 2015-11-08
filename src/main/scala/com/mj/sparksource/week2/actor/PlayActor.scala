package com.mj.sparksource.week2.actor

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import com.mj.sparksource.week2.event.Learning
import com.mj.sparksource.week2.event.Playing

class PlayActor extends Actor {
  val ip = ConfigFactory.load().getConfig("Server").getString("akka.remote.netty.tcp.hostname")
  val name = context.getClass.getName
  override def receive: Receive = {
    case event: Playing =>
      sender ! ip + " PlayActor: I am playing"
      println("PlayActor: I am playing")
  }
}