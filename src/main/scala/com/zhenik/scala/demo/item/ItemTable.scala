package com.zhenik.scala.demo.item

import com.zhenik.scala.demo.JsonProtocol.Item
import com.zhenik.scala.demo.util.db.DatabaseConnector

private[item] trait ItemTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.item.api._

  class Items(tag: Tag) extends Table[Item](tag, "items") {
    def id = column[Long]("id", O.PrimaryKey)
    def name = column[String]("name")

    def * = (id, name) <> ((Item.apply _).tupled, Item.unapply)
  }

  protected val items = TableQuery[Items]
}
