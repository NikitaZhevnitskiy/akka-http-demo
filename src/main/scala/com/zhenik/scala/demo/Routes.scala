package com.zhenik.scala.demo

import akka.Done
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.zhenik.scala.demo.JsonProtocol.{Item, Order}
import scala.concurrent.{ExecutionContext, Future}


class Routes(repository: Repository)(implicit executionContext: ExecutionContext) extends JsonTraitProtocol {

  val itemsRoutes = new ItemsRoutes(repository)

  val route =
    pathPrefix("healthcheck") {
      get {
        complete("OK")
      }
    } ~
      itemsRoutes.route

}

class ItemsRoutes(repository: Repository)(implicit executionContext: ExecutionContext) extends JsonTraitProtocol {

  val route =
    get {
      pathPrefix("item" / LongNumber) { id =>
        // there might be no item for a given id
        val maybeItem: Future[Option[Item]] = repository.fetchItem(id)

        onSuccess(maybeItem) {
          case Some(item) => complete(item)
          case None => complete(StatusCodes.NotFound)
        }
      }
    } ~
      post {
        path("create-order") {
          entity(as[Order]) { order =>
            val saved: Future[Done] = repository.saveOrder(order)
            onComplete(saved) { done =>
              complete("order created")
            }
          }
        }
      }
}
