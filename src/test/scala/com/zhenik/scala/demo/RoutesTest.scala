package com.zhenik.scala.demo

import akka.http.scaladsl.server.Route
import com.zhenik.scala.demo.item.{ItemRepository, ItemRepositoryInMemory, ItemService}
import org.scalatest.Matchers._


class RoutesTest extends BaseServiceTest {

  "HttpRoute" when {
    "GET /healthcheck" should {

      "return 200 OK" in new Context {
        Get("/healthcheck") ~> httpRoute ~> check {
          responseAs[String] shouldBe "OK"
          status.intValue() shouldBe 200
        }
      }

    }
  }

  trait Context {
    val repository: ItemRepository = mock[ItemRepositoryInMemory]
    val itemService: ItemService = mock[ItemService]
    val httpRoute: Route = new Routes(itemService).route
  }
}
