package com.finalproject.nlp

import Sentiment._
import scala.io.Source

/**
  * Created by krbalmryde on 12/10/16.
  */

@deprecated
object SentimentAnalayzerJava {

    val stopWords:Vector[String] = Source.fromFile("src/main/resources/stopwords.txt").getLines.map( _.replaceAll("""(?m)\s+$""", "") ).toVector
    val wordScores:Map[String, Int] = Source.fromFile("src/main/resources/AFINN").getLines.map( ln => {
        val pair = ln.split("\t")
        (pair(0), pair(1).toInt)
    }).toMap

    def apply(tweet:String):(Emotion, Int) = {
        val result:Int = tweet.split("""\s""").filter( wrd => !stopWords.contains(wrd.toLowerCase)).map({
            t => wordScores.get(t) match {
                case Some(num) => num
                case None => 0
            }
        })(collection.breakOut).sum

        (classify(result), result)
    }

    def classify(score:Double): Emotion = {
        score match {
            case s if s < -5.0 => UNKNOWN
            case s if s < -4.0 => VERY_NEGATIVE
            case s if s < -2.0 => NEGATIVE
            case s if s >= -2.0 && s <= 2.0 => NEUTRAL
            case s if s > 2.0 => POSITIVE
            case s if s > 3.0 => VERY_POSITIVE
            case s if s > 5.0 => UNKNOWN
        }
    }
}