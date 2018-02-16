package com.zhenik.scala.demo

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.zhenik.scala.demo.JsonProtocol.{Item, Order}
import spray.json.DefaultJsonProtocol



// domain model
object JsonProtocol {
  final case class Item(name: String, id: Long)
  final case class Order(items: List[Item])
}

trait JsonTraitProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  // formats for unmarshalling and marshalling
  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order)
}


