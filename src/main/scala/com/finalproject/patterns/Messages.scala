package com.finalproject.patterns
import javax.swing.{DefaultListModel, JList}

import scala.collection.JavaConversions._

/**
  * Created by krbalmryde on 12/2/16.
  */
object Messages {
    case class Locations(temp: DefaultListModel[String])
    case object Empty
    case object ShutDown

    case class Trends(woeid:String) // The woeid could be a number OR it could be a String
    case class Query(query:String)
}
