package com.finalproject.patterns

import org.json4s.DefaultFormats

/**
  * Created by krbalmryde on 12/2/16.
  */
object JsonProtocol {
    implicit val formats = DefaultFormats

    case class Tweet(
            id:Int,
            id_str:String,
            text:String,
            truncated:Boolean,
            in_reply_to_status_id:Option[Int],
            in_reply_to_status_id_str:Option[String],
            in_reply_to_user_id:Option[Int],
            in_reply_to_user_id_str:Option[String],
            in_reply_to_screen_name:Option[String]
    )
}
