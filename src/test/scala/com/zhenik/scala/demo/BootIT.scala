package com.zhenik.scala.demo

import com.zhenik.scala.demo.Boot
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import org.scalatest.Matchers._


class BootIT extends BaseServiceTest {

  implicit val sttpBackend = AkkaHttpBackend()

  "Service" should {

    "bind on port successfully and answer on health checks" in {
      awaitForResult(for {
        serverBinding <- Boot.startApplication()
        healthCheckResponse <- sttp.get(uri"http://localhost:8080/healthcheck").send()
        _ <- serverBinding.unbind()
      } yield {
        healthCheckResponse.code shouldBe 200
        healthCheckResponse.body shouldBe Right("OK")
      })
    }

  }
}
