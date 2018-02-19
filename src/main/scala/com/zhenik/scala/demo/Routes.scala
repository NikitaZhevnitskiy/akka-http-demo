package com.zhenik.scala.demo

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.zhenik.scala.demo.JsonProtocol._
import com.zhenik.scala.demo.item.ItemService

import scala.concurrent.ExecutionContext


class Routes(itemService: ItemService)(implicit executionContext: ExecutionContext) extends JsonTraitProtocol {

  import StatusCodes._

  val itemsRoutes = new ItemsRoutes(itemService)

  val route =
    pathPrefix("healthcheck") {
      get {
        complete("OK")
      }
    } ~
      itemsRoutes.route

}

class ItemsRoutes(itemService: ItemService)(implicit executionContext: ExecutionContext) extends JsonTraitProtocol {

  val route =
    pathPrefix("items") {
      pathEnd {
        get {
          val maybeItems = itemService.getItems()
          complete(maybeItems)
        } ~
          post {
            entity(as[Item]) { item =>
           val saved = itemService.createItem(item)
              onSuccess(saved)(_ => complete(item))
            }
          }
      } ~
        path(Segment) { id =>
          get {
            val maybeItem = itemService.getItem(id.toLong)
            onSuccess(maybeItem) {
              case Some(item) => complete(item)
              case None => complete(StatusCodes.NotFound)
            }
          } ~
            put {
              entity(as[Item]) { update =>
                if (update.id != id.toLong)
                  complete(StatusCodes.BadRequest)
                val maybeUpdated = itemService.updateItem(id.toLong, update)
                onSuccess(maybeUpdated) {
                  case Some(item) => complete(s"Item with id:${item.id} was replaced")
                  case None => complete(s"Item with id:${update.id} was created")
                }
              }
            } ~
            delete {
              val newSize = itemService.deleteItem(id.toLong)
              onSuccess(newSize) {
                case 0 => complete(StatusCodes.NotFound)
                case any => complete(s"$any items were deleted")
              }
            }
        }
    }
}
