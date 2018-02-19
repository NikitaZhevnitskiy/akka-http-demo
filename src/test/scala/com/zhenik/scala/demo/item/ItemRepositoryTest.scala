package com.zhenik.scala.demo.item

import com.zhenik.scala.demo.BaseServiceTest
import com.zhenik.scala.demo.JsonProtocol.Item

class ItemRepositoryTest extends BaseServiceTest {

  "Repository test" when {

    "fetchItems" should {

      "orders is empty" in {
        // Arrange
        val repository = new ItemRepositoryImpl()
        // Act Assert
        repository.fetchItems().onComplete(items => assert(items.get.isEmpty))
      }

      "add item to orders" in {
        // Arrange
        val repository = new ItemRepositoryImpl()
        repository.orders=List(Item("name",1))
        // Act Assert
        repository.fetchItems().onComplete(items => assert(items.get.nonEmpty))
      }

      "fetch item by id" in {
        // Arrange
        val repository = new ItemRepositoryImpl()
        repository.addItem(Item("name",1))
        // Act Assert
        repository.fetchItem(1).onComplete(item => assert(item.get.get.name=="name"))
      }

      "fetch item that is not exist" in {
        // Arrange
        val repository = new ItemRepositoryImpl()
        repository.addItem(Item("name",1))

        // Act Assert
        try {
          repository.fetchItem(2).onComplete(item => item.getOrElse())
          fail()
        } catch {
          case e : Exception => //println(e)
        }

      }

    }

  }


}
