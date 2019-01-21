package io.vlingo.hike.domain.hiker

data class Step(val x: Double, val y: Double, val z: Double)
data class Route(val steps: List<Step>)