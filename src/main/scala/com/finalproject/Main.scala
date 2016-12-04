package com.finalproject

import twitter4j._
import com.finalproject.actors._
import com.finalproject.Utils.instructions

import scala.io.StdIn
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.finalproject.patterns.Messages.{Empty, Locations, Search}
import com.finalproject.tweeter.{Tweeter, TweeterStatusListener}

import scala.collection.JavaConversions._


// Twitter Hello
object Main {

    def main(args: Array[String]): Unit = {

        implicit val system = ActorSystem("FinalProject")
        implicit val materializer = ActorMaterializer()
        import system.dispatcher

        val masterActor = system.actorOf(Props[MasterActor], name = "master")


        // RawStream is Good, but we can do better I think...
        //    twitterStream.addListener(
        //        new RawStreamListener {
        //            override def onMessage(rawString: String): Unit = println(s"tweet: $rawString")
        //            override def onException(ex: Exception): Unit = ex.printStackTrace()
        //        }
        //    )
        //    twitterStream.sample()
//        val Trends = """(t|trend[s]*)$""".r
//        val TrendID = """(t$|trends)(\s+[0-9]+|)""".r

        println(instructions)
        while (true) {
            StdIn.readLine(s">: ") match {
                case "q" | "quit" =>
                    println(s"Thanks for playing!")
                    return

                case "h" | "help" =>
                    println(instructions)

                case "t" | "trends" =>
                    masterActor ! Locations // User is requestion a list of all Possible trending locations

                case input: String =>
                    println("Down the rabbit hole we go!")
                    masterActor ! Search(input)

                case _ =>
                    // If they simply hit enter, we treat it like they want EVERYTHING
                    masterActor ! Empty
            }
        }


    }
}


