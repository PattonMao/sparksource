package com.mj.sparksource.week2.actor

import akka.actor.Actor
import com.mj.sparksource.week2.event._

class ClientAcotr extends Actor {
  val work1 = context.actorSelection("akka.tcp://Server@192.168.0.110:5101/user/learnActor")
  val work2 = context.actorSelection("akka.tcp://Server@192.168.0.110:5101/user/listenActor")
  val work3 = context.actorSelection("akka.tcp://Server@192.168.0.110:5101/user/workActor")
  val work4 = context.actorSelection("akka.tcp://Server@192.168.0.110:5101/user/playActor")

  override def receive: Receive = {
    case event: Learning =>
      work1 ! event
    case event: Listening =>
      work2 ! event
    case event: Working =>
      work3 ! event
    case event: Playing =>
      work4 ! event
    case reply: String =>
      println(reply)
    case _ =>
      println("I am thinking...what should I do")
  }
}
