package com.zhenik.scala.demo.item

import com.zhenik.scala.demo.JsonProtocol.Item

import scala.concurrent.{ExecutionContext, Future}

class ItemService(itemRepository: ItemRepository)(implicit executionContext: ExecutionContext) {

  def getItems(): Future[Seq[Item]] =
    itemRepository.fetchItems()

  def getItem(id: Long): Future[Option[Item]] =
    itemRepository.fetchItem(id)

  def createItem(item: Item): Future[Option[Long]] =
    itemRepository.addItem(item)

  def updateItem(id:Long, item: Item): Future[Option[Item]] =
    itemRepository.updateItem(id, item)

  def deleteItem(id: Long): Future[Int] =
    itemRepository.deleteItem(id)

}
