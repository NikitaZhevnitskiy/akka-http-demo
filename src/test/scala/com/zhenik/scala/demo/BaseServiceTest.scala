package com.zhenik.scala.demo

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import org.scalatest.Matchers._

trait BaseServiceTest extends WordSpec with ScalatestRouteTest with MockitoSugar {

  def awaitForResult[T](futureResult: Future[T]): T =
    Await.result(futureResult, 5.seconds)
}

