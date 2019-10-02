package com.example.vlingohike.domain.journey

import com.google.gson.Gson
import io.vlingo.actors.Actor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.*

class JourneyActor(val id: UUID, val host: String): Actor(), Journey {
    private val client = OkHttpClient()
    private val gson: Gson = Gson()

    override fun step(step: Step) {
        val request = Request.Builder()
            .method("PUT", RequestBody.create(MediaType.get("application/json"), gson.toJson(step)))
            .url("http://$host/journey/$id/step")
            .build()

        client.newCall(request).execute()
    }

    override fun inDanger() {
        val request = Request.Builder()
            .method("PUT", RequestBody.create(MediaType.get("application/json"), ""))
            .url("http://$host/journey/$id/alarm")
            .build()

        client.newCall(request).execute()
    }

    override fun safe() {
        val request = Request.Builder()
            .method("DELETE", null)
            .url("http://$host/journey/$id/alarm")
            .build()

        client.newCall(request).execute()
    }
}