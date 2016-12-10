package com.finalproject.nlp

/**
  * Created by krbalmryde on 12/10/16.
  */

sealed trait Emotion

object Sentiment {
    case object UNKNOWN extends Emotion
    case object VERY_NEGATIVE extends Emotion
    case object NEGATIVE extends Emotion
    case object NEUTRAL extends Emotion
    case object POSITIVE extends Emotion
    case object VERY_POSITIVE extends Emotion

}
