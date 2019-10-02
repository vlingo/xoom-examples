package com.example.vlingohike.domain.journey

interface Journey {
    fun step(step: Step)
    fun inDanger()
    fun safe()
}