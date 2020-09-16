package springsimulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

import scala.util.Random


class SpringSimulation extends Simulation{



  // http://192.168.178.118   Linux System
  // http://192.168.178.59   Windows 


    val rnd = new Random()
  def randomString(lenght: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(lenght).mkString
  }

  val greetingFeeder = Iterator.continually(Map(
    "messageContent" -> randomString(10),
    "descriptionContent" -> randomString(20),
    "valueContent" -> randomString(8)
  ))

  def testCase1() = {
    exec(http("say hello")
    .get("/hello")
    .check(status.is(200)))
  }

  def testCase2() = {
        repeat(15){
    exec(http("Say hello")
    .get("/hello")
    .check(status.is(200)))
  }
  }

  def testCase3() = {
    feed(greetingFeeder)
    .exec(http("Add new Greeting")
    .post("/greetings")
    .body(ElFileBody("greeting.json"))
    .check(status.is(200))
    .check(jsonPath("$.id").saveAs("greetingId")))
    .pause(3)
  // change the greeting message
      .exec(http("Change message")
      .patch("/greetings/${greetingId}/message")
      .body(ElFileBody("greetingUpdate.json"))
      .check(status.is(200)))
      .pause(2)
  // change the greeting description
      .exec(http("Change description")
      .patch("/greetings/${greetingId}/description")
      .body(ElFileBody("greetingUpdate.json"))
      .check(status.is(200)))
  }

  val httpProtocol = http
    .baseUrl("http://192.168.178.118:8080")
    .acceptHeader("application/json,")
    .contentTypeHeader("application/json")

  val testCase1Scenario = scenario("Spring Test Case 1").exec(testCase1())
  val testCase2Scenario = scenario("Spring Test Case 2").exec(testCase2())
  val testCase3Scenario = scenario("Spring Test Case 3").exec(testCase3())

  setUp(

    testCase3Scenario.inject(
      nothingFor(4 seconds),
      rampUsers(10000) during(5 minutes) 
      ).protocols(httpProtocol)

  //   testCase2Scenario.inject(
  //   nothingFor(4 seconds),
  //    constantUsersPerSec(500) during(5 minutes) 
  //   ).protocols(httpProtocol)

  //     testCase3Scenario.inject(
  //    nothingFor(4 seconds),
  //    constantUsersPerSec(500) during(5 minutes)
  //    ).protocols(httpProtocol)

  ).maxDuration(320 seconds)


}

