import sbt.Keys._

lazy val root = (project in file("."))
        .settings(
            name := "final_project",
            version := "1.0",
            scalaVersion := "2.11.8",

            // For ScalaTest, disables the buffered Output offered by sbt and uses its own method
            logBuffered in Test := false,

            libraryDependencies ++= Seq(
                "org.json4s" %% "json4s-native" % "3.3.0",                                  // For Json parsing

                "com.typesafe.akka" %% "akka-actor" % "2.4.11",                             // Akka actores
                "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",                 // Akka http stuffs

                // Go here in the event you need more jars
                // https://mvnrepository.com/artifact/org.twitter4j
                "org.twitter4j" % "twitter4j-core" % "4.0.5",                               // Twitter4j Core Twitter API
                "org.twitter4j" % "twitter4j-stream" % "4.0.5",                             // Twitter4j Streaming API
                "org.twitter4j" % "twitter4j-async" % "4.0.5",                              // Twitter4j Async API, needed for Streaming

                /*             Unit Test libraries             */
                "com.typesafe.akka" %% "akka-testkit" % "2.4.11",                           // Akka-testkit
                "org.scalactic" %% "scalactic" % "3.0.0",                                   // scalactic
                "org.scalatest" %% "scalatest" % "3.0.0" % "test"                           // scalatest
            )
        )

