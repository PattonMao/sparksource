package com.mj.sparksource.week2.actor

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import com.mj.sparksource.week2.event.Learning
import com.mj.sparksource.week2.event.Working

class WorkActor extends Actor {
  val ip = ConfigFactory.load().getConfig("Server").getString("akka.remote.netty.tcp.hostname")
  val name = context.getClass.getName
  override def receive: Receive = {
    case event: Working =>
      sender ! ip + " WorkActor: I am working"
      println("WorkActor: I am working")
  }
}