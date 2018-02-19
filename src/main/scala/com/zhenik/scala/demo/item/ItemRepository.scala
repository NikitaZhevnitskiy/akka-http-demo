package com.zhenik.scala.demo.item

import com.zhenik.scala.demo.JsonProtocol.Item
import com.zhenik.scala.demo.util.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}


trait ItemRepository {
  def fetchItem(itemId: Long): Future[Option[Item]]

  def fetchItems(): Future[List[Item]]

  def addItem(item: Item): Future[Item]

  def updateItem(id: Long, item: Item): Future[Option[Item]]

  def deleteItem(id: Long): Future[Int]
}

class ItemRepositoryPostgres(
                              val databaseConnector: DatabaseConnector
                            )(implicit executionContext: ExecutionContext) extends ItemTable with ItemRepository {

  import databaseConnector._
  import databaseConnector.item.api._


  def fetchItem(itemId: Long): Future[Option[Item]] =
    db.run(items.filter(_.id === itemId).result.headOption)

  def fetchItems(): Future[List[Item]] =
    db.run(items.result).map(col => col.toList)


  def addItem(item: Item): Future[Item] =
    db.run(items.insertOrUpdate(item)).map(_ => item)

  def updateItem(id: Long, item: Item): Future[Option[Item]] =
    db.run(items.update(item)).map(_ => Option.apply(item))


  def deleteItem(id: Long): Future[Int] = {
    val q = items.filter(_.id === id)
    val action = q.delete
    val affectedRowsCount: Future[Int] = db.run(action)
    affectedRowsCount
  }
}

class ItemRepositoryInMemory(implicit executionContext: ExecutionContext) extends ItemRepository {

  var orders: List[Item] = Nil
  // (fake) async database query api

  def fetchItem(itemId: Long): Future[Option[Item]] = Future {
    orders.find(o => o.id == itemId)
  }

  def fetchItems(): Future[List[Item]] = Future {
    orders
  }

  def addItem(item: Item): Future[Item] = {
    Future.successful {
      orders = orders.filterNot(_.id == item.id)
      orders = orders :+ item
      item
    }
  }

  def updateItem(id: Long, item: Item): Future[Option[Item]] = Future {
    val replacedItem = Item(id, item.name)
    orders.indexWhere(_.id == id) match {
      case -1 =>
        orders = orders :+ replacedItem
        Option.empty
      case any =>
        orders = orders.updated(any, replacedItem)
        Option.apply(item)
    }
  }

  /**
    * Remove all entries of element in collection
    *
    * @return new size of collection
    **/
  def deleteItem(id: Long): Future[Int] = Future {
    val oldSize = orders.size
    orders = orders.filterNot(_.id == id)
    oldSize - orders.size
  }

}
