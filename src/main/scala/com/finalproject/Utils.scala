package com.finalproject

import java.io.File

import twitter4j.conf.{Configuration, ConfigurationBuilder}

import scala.sys.process._


/**
  * Created by krbalmryde on 12/1/16.
  */
package object Utils {

    // This is the United States YAHOO! Where On Earth Id
    // Used for obtaining trending tweets or hashtags
    val USWOEID = 23424977

    /**
      * Convenience function which gives me the current working directory
      * @return String
      */
    def pwd:String = System.getProperty("user.dir")


    /**
      * Name:
      *     ParseFilesInDir
      *
      * Description:
      *     Recursively parses Files in the local project Resources/ directory producing
      *     an array of Strings containing file paths to each of the source files
      *     found.
      *
      * Source:
      *     This function was adapted from the accepted answer of this StackOverflow question
      *     http://stackoverflow.com/questions/2637643/how-do-i-list-all-files-in-a-subdirectory-in-scala
      *
      * @param dir: a Java File object containing the source to the directory
      * @return Array[String]
      */
    def parseFilesInDir(dir: File): Array[File] = {
        val files = dir.listFiles
        val allFiles = files ++ files.filter(_.isDirectory).flatMap(parseFilesInDir)
        allFiles.filter( f => """.*\.java$""".r.findFirstIn(f.getName).isDefined)
    }


}