package com.zhenik.scala.demo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.zhenik.scala.demo.item.{ItemRepositoryInMemory, ItemRepositoryPostgres, ItemService}
import com.zhenik.scala.demo.util.Config
import com.zhenik.scala.demo.util.db.{DatabaseConnector, DatabaseMigrationManager}

object Boot extends App {
  def startApplication() = {

    // needed to run the route
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
    implicit val executionContext = system.dispatcher

    // conf
    val config = Config.load()


    // migration
    println(config.database)

    new DatabaseMigrationManager(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    ).migrateDatabaseSchema()

    val databaseConnector = new DatabaseConnector(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    )

    // dependecies
//    val itemRepository = new ItemRepositoryInMemory()
    val itemRepository = new ItemRepositoryPostgres(databaseConnector)
    val itemService = new ItemService(itemRepository)
    val httpRoute = new Routes(itemService)

    val bindingFuture = Http().bindAndHandle(httpRoute.route, config.http.host, config.http.port)
    bindingFuture
  }

  startApplication()
}