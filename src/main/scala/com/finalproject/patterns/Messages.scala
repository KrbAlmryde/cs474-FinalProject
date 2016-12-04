package com.finalproject.patterns
import scala.collection.JavaConversions._

/**
  * Created by krbalmryde on 12/2/16.
  */
object Messages {
    case object Locations
    case object Empty

    case class Trends(woeid:String) // The woeid could be a number OR it could be a String
    case class Search(query:String)
}
