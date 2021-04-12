package io.vlingo.hike

import io.restassured.RestAssured
import org.testcontainers.containers.PostgreSQLContainer

abstract class Specification {
    companion object {
        private val postgreSql = PostgreSQLContainer<Nothing>()
        private val port = 9092
        private val baseUrl = "http://localhost:$port"
        private val applicationServer: ApplicationServer

        init {
            postgreSql.start()
            RestAssured.baseURI = baseUrl
            RestAssured.config.httpClientConfig.dontReuseHttpClientInstance()

            applicationServer = ApplicationServer(
                postgreSql.jdbcUrl, postgreSql.username, postgreSql.password,
                port, ssl = false, alarmInterval = 1L
            )
            applicationServer.start()
        }
    }
}