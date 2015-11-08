package com.mj.sparksource.week2

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.mj.sparksource.week2.actor.WorkActor
import akka.actor.Props
import com.mj.sparksource.week2.actor.LearnActor
import com.mj.sparksource.week2.actor.ListenActor
import com.mj.sparksource.week2.actor.PlayActor

object Server {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Server", ConfigFactory.load().getConfig("Server"))
    val learnActor = system.actorOf(Props[LearnActor], name = "learnActor")
    val listenActor = system.actorOf(Props[ListenActor], name = "listenActor")
    val workActor = system.actorOf(Props[WorkActor], name = "workActor")
    val playActor = system.actorOf(Props[PlayActor], name = "playActor")
  }
}