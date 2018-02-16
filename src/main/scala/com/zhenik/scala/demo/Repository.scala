package com.zhenik.scala.demo

import akka.Done
import com.zhenik.scala.demo.JsonProtocol.{Item, Order}

import scala.concurrent.{ExecutionContext, Future}

sealed trait Repository {
  def fetchItem(itemId: Long): Future[Option[Item]]
  def saveOrder(order: Order): Future[Done]
}

class RepositoryImpl()(implicit executionContext: ExecutionContext) extends Repository {

  var orders: List[Item] = Nil
  // (fake) async database query api

  def fetchItem(itemId: Long): Future[Option[Item]] = Future {
    orders.find(o => o.id == itemId)
  }

  def saveOrder(order: Order): Future[Done] = {
    orders = order match {
      case Order(items) => items ::: orders
      case _            => orders
    }
    Future { Done }
  }
}
