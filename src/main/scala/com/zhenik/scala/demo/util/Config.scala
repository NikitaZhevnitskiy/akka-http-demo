package com.zhenik.scala.demo.util

import pureconfig.loadConfig

case class Config(http: HttpConfig, database: DatabaseConfig)

object Config {
  def load() =
    loadConfig[Config] match {
      case Right(config) => config
      case Left(error) =>
        throw new RuntimeException("Cannot read config file, errors:\n" + error.toList.mkString("\n"))
    }
}


private[util] case class HttpConfig(host: String, port: Int)
private[util] case class DatabaseConfig(jdbcUrl: String, username: String, password: String)
