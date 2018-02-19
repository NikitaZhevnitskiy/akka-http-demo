package com.zhenik.scala.demo.item

import akka.Done
import com.zhenik.scala.demo.JsonProtocol.{Item, Order}

import scala.concurrent.{ExecutionContext, Future}


trait ItemRepository {
  def fetchItem(itemId: Long): Future[Option[Item]]
  def fetchItems(): Future[List[Item]]
  def saveOrder(order: Order): Future[Done]
  def addItem(item: Item): Future[Option[Long]]
  def updateItem(id:Long, item: Item): Future[Option[Item]]
  def deleteItem(id: Long): Future[Int]
}

class ItemRepositoryImpl(implicit executionContext: ExecutionContext) extends ItemRepository {

  var orders: List[Item] = Nil
  // (fake) async database query api

  def fetchItem(itemId: Long): Future[Option[Item]] = Future {
    orders.find(o => o.id == itemId)
  }
  def fetchItems(): Future[List[Item]] = Future {
    orders
  }

  def addItem(item: Item): Future[Option[Long]] = Future{
    orders.find(_.id == item.id) match  {
      case Some(i) => None // id conflict
      case None =>
        orders = orders :+ item
        Some(item.id)
    }
  }

  def updateItem(id: Long, item: Item): Future[Option[Item]] = Future {
    val replacedItem = Item(item.name,id)
    orders.indexWhere(_.id == id ) match {
      case -1 =>
        orders = orders :+ replacedItem
        Option.empty
      case any  =>
        orders=orders.updated(any, replacedItem)
        Option.apply(item)
    }
  }

  /**
    * Remove all entries of element in collection
    * @return new size of collection
    * */
  def deleteItem(id: Long): Future[Int] = Future {
    val oldSize = orders.size
    orders = orders.filterNot(_.id == id)
    oldSize-orders.size
  }

  def saveOrder(order: Order): Future[Done] = {
    orders = order match {
      case Order(items) => items ::: orders
      case _            => orders
    }
    Future { Done }
  }


}
