package com.example.vlingohike.domain.alarm

import com.google.gson.Gson
import io.vlingo.actors.Actor
import io.vlingo.common.Scheduled
import okhttp3.OkHttpClient
import okhttp3.Request

class AlarmServiceActor(val listener: AlarmListener, val interval: Long, val host: String, var alarms: List<Alarm>) : Actor(), AlarmService, Scheduled<Unit> {
    private val client = OkHttpClient()
    private val gson = Gson()

    override fun run() {
        scheduler().schedule(selfAs(Scheduled::class.java) as Scheduled<Unit>, Unit, 0L, interval)
    }

    override fun intervalSignal(scheduled: Scheduled<Unit>, data: Unit) {
        val request = Request.Builder()
            .url("http://$host/alarms")
            .build()

        val snapshotContent = client.newCall(request).execute().body()!!.string()
        val snapshot = gson.fromJson<AlarmsSnapshot>(snapshotContent, AlarmsSnapshot::class.java)

        val receivedAlarms = snapshot.journeys.map {
            Alarm(it.journey, it.steps.last(), it.whenStarted)
        }

        receivedAlarms.forEach(listener::onAlarm)

        val receivedIds = receivedAlarms.map { it.journey }
        val storedIds = alarms.map { it.journey }

        val toAck = storedIds - receivedIds
        toAck.forEach { listener.onAcknowledge(Acknowledge(it)) }

        alarms = receivedAlarms
    }
}