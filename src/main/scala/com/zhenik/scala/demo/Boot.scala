package com.zhenik.scala.demo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.zhenik.scala.demo.item.{ItemRepositoryImpl, ItemService}

object Boot extends App {
  def startApplication() = {
    // needed to run the route
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
    implicit val executionContext = system.dispatcher
    val config = Config.load()


    val itemRepository = new ItemRepositoryImpl()
    val itemService = new ItemService(itemRepository)
    val httpRoute = new Routes(itemService)

    val bindingFuture = Http().bindAndHandle(httpRoute.route, config.http.host, config.http.port)
    bindingFuture
  }

  startApplication()
}