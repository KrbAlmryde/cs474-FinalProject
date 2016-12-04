package com.finalproject

import twitter4j._
import com.finalproject.actors._
import com.finalproject.Utils.instructions

import scala.io.StdIn
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.finalproject.patterns.Messages.{Empty, Locations, ShutDown}




// Twitter Hello
object Main extends App {

        implicit val system = ActorSystem("FinalProject")
        implicit val materializer = ActorMaterializer()

        val masterActor = system.actorOf(Props[MasterActor], name = "master")

        println(instructions)
        while (true) {
            StdIn.readLine(s">: ") match {
                case "q" | "quit" =>
                    println(s"Thanks for playing!")
                    masterActor ! ShutDown

                case "h" | "help" =>
                    println(instructions)

                case "t" | "trends" =>
                    masterActor ! Locations // User is requestion a list of all Possible trending locations

                case input: String =>
                    input match {
                        // If they simply hit enter, we treat it like they want EVERYTHING
                        case "" =>
                            masterActor ! Empty
                        case _ =>
                            println("Down the rabbit hole we go!")
                            masterActor ! input
                    }
            }
        }

}


