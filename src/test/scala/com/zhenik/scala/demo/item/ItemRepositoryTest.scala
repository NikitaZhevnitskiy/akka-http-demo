package com.zhenik.scala.demo.item

import com.zhenik.scala.demo.BaseServiceTest
import com.zhenik.scala.demo.JsonProtocol.Item

class ItemRepositoryTest extends BaseServiceTest {

  "Repository test" when {

    "fetchItems" should {

      "orders is empty" in {
        // Arrange
        val repository = new ItemRepositoryInMemory()
        // Act Assert
        repository.fetchItems().onComplete(items => assert(items.get.isEmpty))
      }

      "add item to orders" in {
        // Arrange
        val repository = new ItemRepositoryInMemory()
        repository.orders=List(Item(1,"name"))
        // Act Assert
        repository.fetchItems().onComplete(items => assert(items.get.nonEmpty))
      }

      "fetch item by id" in {
        // Arrange
        val repository = new ItemRepositoryInMemory()
        repository.addItem(Item(1, "name"))
        // Act Assert
        repository.fetchItem(1).onComplete(item => assert(item.get.get.name=="name"))
      }

      "fetch item that is not exist" in {
        // Arrange
        val repository = new ItemRepositoryInMemory()
        repository.addItem(Item(1,"name"))

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
