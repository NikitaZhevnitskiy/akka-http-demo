package com.zhenik.scala.demo

import akka.Done
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.zhenik.scala.demo.JsonProtocol._

import scala.concurrent.ExecutionContext


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
    pathPrefix("items") {
      pathEnd {
        get {
          val maybeItems = repository.fetchItems()
          onSuccess(maybeItems) {
            case Some(items) => complete(items)
            case None => complete(StatusCodes.NotFound)
          }
        } ~
        post {
          entity(as[Item]) { item =>
            val saved = repository.addItem(item)
            onSuccess(saved) {
              case Some(it) => complete(s"Item with id:$it was created")
              case None => complete(StatusCodes.NotFound)
            }
          }
        }
      } ~
      path(Segment) { id =>
        get {
          val maybeItem = repository.fetchItem(id.toLong)
          onSuccess(maybeItem) {
            case Some(item) => complete(item)
            case None       => complete(StatusCodes.NotFound)
          }
        } ~
        put {
          entity(as[Item]) { update =>
            if(update.id != id.toLong)
              complete(StatusCodes.BadRequest)
            val maybeUpdated = repository.updateItem(id.toLong,update)
            onSuccess(maybeUpdated){
              case Some(item) => complete(s"Item with id:${item.id} was replaced")
              case None       => complete(s"Item with id:${update.id} was created")
            }
          }
        } ~
        delete {
          val newSize = repository.deleteItem(id.toLong)
          onSuccess(newSize) {
            case 0 => complete(StatusCodes.NotFound)
            case any => complete(s"$any items were deleted")
          }
        }
      }
    }









//    get {
//      pathPrefix("item" / LongNumber) { id =>
//        // there might be no item for a given id
//        val maybeItem: Future[Option[Item]] = repository.fetchItem(id)
//
//        onSuccess(maybeItem) {
//          case Some(item) => complete(item)
//          case None => complete(StatusCodes.NotFound)
//        }
//      }
//    } ~
//      post {
//        path("create-order") {
//          entity(as[Order]) { order =>
//            val saved: Future[Done] = repository.saveOrder(order)
//            onComplete(saved) { done =>
//              complete("order created")
//            }
//          }
//        }
//      }
}
