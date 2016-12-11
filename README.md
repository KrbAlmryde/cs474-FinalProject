# CS 474: Final Project

### Authors
+ Kyle Almryde
+ Abhishek
+ Mike

#### Preamble

This README documents the installation and usage for the cs474 Final Project.


### What is this repository for? ###

Using the [Scala language](http://www.scala-lang.org/), [Akka](https://akka.io) Actor system, and [Twitter4j](https://github.com/yusuke/twitter4j), a java based wrapper around the Twitter API. This application streams Twitter data based on user input (which is guided by the application)


### Libraries
* [Akka](https://akka.io) was used for the Actor system and HttpRequests
* [json4s](https://github.com/json4s/json4s) was used to parse JSON like a boss
* [twitter4j](https://github.com/yusuke/twitter4j) was used to interface with the Twitter API for streaming Tweets

### How do I get set up?
To run the application, navigate to the root level of the project
```
cd kyle_mike_abhishek_finalproject/
sbt run
```
Give it a minute to load then a web browser **SHOULD** open. If not, open (may I suggest Chrome?) at http://localhost:9000/ or [click here!](http://localhost:9000/) 

###Full Disclosure
If you have a poor internet connection, you are going to have a bad time. Dont let ComCast get you down!
Joking aside, this app requires a solid internet connection in order to do its job. The app may not shutdown in some cases...

####Setting up from IntelliJ ####

1) If no project is currently open in IntelliJ IDEA, click **Import Project** on the Welcome screen. Otherwise, select **File | New | Project from Existing Sources**.

2) In the dialog that opens, select the directory that contains the project to be imported, or a file that contains an appropriate project description. Click **OK**.

3) On the first page of the **Import Project** wizard, select SBT, and click **Next**. (This page is not shown if IntelliJ IDEA has guessed what you are importing.)

4) On the next page of the wizard, specify SBT project settings and global SBT settings, click **Finish**.


#### Development Testing
This project turned out to be particularly challenging in a large part due to the way Twitter4j handles the data streams.
That is, it uses the Listener/Callback model for handling incoming streams from Twitter, which acts in direct conflict with 
the Reactive Streaming model that Akka incorporates. 

##### Generating a Source 
To get around this, I utilized a hybrid actor model wherein I create a specially designed Actor to act as a 
'Publisher Source'. What this means is the actor sits inside of the Callback function and emits the incoming tweets as 
they arrive. The advantage of this approach is it let me process those tweets in whatever way I wanted to right away. 
From that point the pipeline is pretty standard Akka streaming approach. it incorporates a largely old school 
Listener/Callback Model which, while great in the conventional sense, proves tricky once you start trying to integrate 
it into a reactive model something like Akka's streaming library. 

##### Going with the Flow
Once our Source is generated, it was pretty straight forward to implement the nodes within Flow Graph. For my purposes,
I only include 1 Flow junction, which is used to perform the sentiment analysis. The benefits of doing it the way I did
was it automagically handles backpressue from the system. 


#### Unit Testing
For Unit-testing I utilized Akka's Actor Testkit[ScalaTest](http://www.scalatest.orge) using the [FunSuite](http://doc.scalatest.org/3.0.0/#org.scalatest.FunSuite). Its fun and was surprisingly simple to get it up and running. The Akka Testkit was no walk in the park though, so forgive the stupid simple tests.

Go figure.

To run the tests, in Intellij simplly select the **SBT** task "Test" and youll be up and running! Que sera


## Discussion:
### How to tell something is happening

### Actor Model
I designed the Actor Model in the following way. The basic idea here is that each
step in the pipeline is controlled by and actor. Actions like Invoking the Github
API or generating the Patch and Understand Dependency graph each got their own actor.
My aim was to encapsulate as much of the task within the particular actor as I could
manage. For actions that invoked the system shell I created a separate 'Process Actor'.
For my purposes there was only one Github actor, it performed the query, and piped the results
back to the overseeing master actor, who then distributed the resulting json blobs to a dedicated
repository manager actor. This actor was responsible for overseeing the generation of the Understand database,
Patch file, and eventually composing and joining both of those pieces of information. Finally, it takes this
composition, generates a report, and sends THAT to the Master actor whom is overseeing all of these child
Repository actors, prints the results to Standard output, then shuts down the repository (deleting the contents of
the folder as well). It aint perfect by any measure of the word, but it works **mostly**
![Actor Model](docs/Almryde_ActorModel.png)


### Who do I talk to? ###

* If you have any specific questions contact me via [kyle.almryde@gmail.com](mailto:kyle.almryde@gmail.com)
* If you have any complaints, please direct them to this [Handsome devil](mailto:drmark@uic.edu)

![drmark@uic.edu](https://www.cs.uic.edu/~drmark/index_htm_files/3017.jpg)




