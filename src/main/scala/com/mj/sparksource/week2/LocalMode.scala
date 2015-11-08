package com.mj.sparksource.week2

import com.mj.sparksource.week2.event.EventProcessLoop
import com.mj.sparksource.week2.event.Listening
import com.mj.sparksource.week2.event.Learning
import com.mj.sparksource.week2.event.Playing
import com.mj.sparksource.week2.event.Working
import scala.util.Random
import com.mj.sparksource.week2.event.LocalProcessLoop

object LocalMode {
  def main(args: Array[String]): Unit = {
    val eventLoop = new LocalProcessLoop("local mode")
    val eventList = List(Learning(), Listening(), Working(), Playing())

    val producer = new Thread() {
      override def run(): Unit = {
        try {
          while (true) {
            val r = Random
            eventLoop.post(eventList(r.nextInt(3)))
            Thread.sleep(200)
          }
        } catch {
          case ie: InterruptedException => {
            println("producer stop !")
            return
          }
          case _ => return
        }
      }
    }

    eventLoop.start
    Thread.sleep(2000)
    producer.start
    Thread.sleep(2000)
    producer.interrupt
    eventLoop.stop

  }
}