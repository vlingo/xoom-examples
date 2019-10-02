package io.vlingo.hike

fun main() {
    ApplicationServer(
        "jdbc:postgresql://postgres.service:5432/hike",
        "hike",
        "hike",
        8080,
        ssl = false,
        alarmInterval = 500L
    ).start()
}