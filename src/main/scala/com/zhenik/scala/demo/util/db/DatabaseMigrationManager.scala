package com.zhenik.scala.demo.util.db

import org.flywaydb.core.Flyway

class DatabaseMigrationManager(jdbcUrl: String, dbUser: String, dbPassword: String) {
  private val flyway = new Flyway()
  println(s"$jdbcUrl|$dbUser|$dbPassword|")
  flyway.setDataSource(jdbcUrl, dbUser, dbPassword)


  def migrateDatabaseSchema() : Unit = println(flyway.migrate())

  def dropDatabase() : Unit = flyway.clean()

}
