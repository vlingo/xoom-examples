package com.example.vlingohike.domain.alarm

import java.util.*

data class AlarmStep(val altitude: Double, val longitude: Double, val latitude: Double)
data class AlarmJourney(val journey: UUID, val steps: List<AlarmStep>, val whenStarted: Long)
data class AlarmsSnapshot(val journeys: List<AlarmJourney>)

data class Alarm(val journey: UUID, val step: AlarmStep, val whenStarted: Long)
data class Acknowledge(val journey: UUID)

interface AlarmListener {
    fun onAlarm(alarm: Alarm)
    fun onAcknowledge(acknowledge: Acknowledge)
}

interface AlarmService {
    fun run()
}