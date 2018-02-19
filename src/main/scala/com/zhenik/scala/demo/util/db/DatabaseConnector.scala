package com.zhenik.scala.demo.util.db

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

class DatabaseConnector(jdbcUrl:String, dbUser: String, dbPassword: String) {

  private val hikariDataSource = {
    val hikariCongif = new HikariConfig()
    hikariCongif.setJdbcUrl(jdbcUrl)
    hikariCongif.setUsername(dbUser)
    hikariCongif.setPassword(dbPassword)

    new HikariDataSource(hikariCongif)
  }

  val item = slick.jdbc.PostgresProfile
  import item.api._

  val db = Database.forDataSource(hikariDataSource, None)
  db.createSession()
}
