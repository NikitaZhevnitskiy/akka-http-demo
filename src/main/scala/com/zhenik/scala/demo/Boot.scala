package com.zhenik.scala.demo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Boot extends App {
  def startApplication() = {
    // needed to run the route
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
    implicit val executionContext = system.dispatcher

    val repository = new RepositoryImpl()
    val httpRoute = new Routes(repository)

    val bindingFuture = Http().bindAndHandle(httpRoute.route, "localhost", 8080)
  }

  startApplication()
}