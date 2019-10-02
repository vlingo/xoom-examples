package com.example.vlingohike

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.util.Log
import com.example.vlingohike.domain.alarm.*
import com.example.vlingohike.domain.journey.Journey
import com.example.vlingohike.domain.journey.JourneyActor
import com.example.vlingohike.domain.journey.Step
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.vlingo.actors.World
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, AlarmListener, LocationListener {
    private val BACKEND: String = "<YOUR_BACKEND_IP>"

    private lateinit var journeyId: UUID
    private lateinit var journey: Journey
    private lateinit var alarmService: AlarmService
    private val world: World = World.startWithDefaults("android")
    private var myJourney: Polyline? = null
    private var alarmedPositions: Map<UUID, Marker> = emptyMap()

    private lateinit var mainHandler: Handler
    private lateinit var googleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        journeyId = UUID.randomUUID()
        journey = world.actorFor(Journey::class.java, JourneyActor::class.java, journeyId, BACKEND)
        alarmService = world.actorFor(AlarmService::class.java, AlarmServiceActor::class.java, this, 500L, BACKEND, emptyList<Alarm>())
        alarmService.run()

        Log.i("Main", "My journeyId is $journeyId")
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        mainHandler = Handler(mainLooper)

        raiseAlarm.show()
        acknowledge.hide()

        raiseAlarm.setOnClickListener {
            journey.inDanger()
            raiseAlarm.hide()
            acknowledge.show()
        }

        acknowledge.setOnClickListener {
            journey.safe()
            raiseAlarm.show()
            acknowledge.hide()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            0.1f,
            this
        )

        myJourney = googleMap.addPolyline(PolylineOptions()
            .startCap(RoundCap())
            .endCap(RoundCap())
            .width(30f)
            .color(Color.BLUE))
    }

    override fun onAlarm(alarm: Alarm) {
        mainHandler.post {
            val journey = alarm.journey
            val wasAlarmed = alarmedPositions.containsKey(journey)

            if (wasAlarmed) {
                val marker = alarmedPositions[journey]!!
                if (alarm.step.latitude == marker.position.latitude && alarm.step.longitude == marker.position.longitude) {
                    if (marker.snippet != "${DateUtils.getRelativeTimeSpanString(alarm.whenStarted)}") {
                        marker.snippet = "${DateUtils.getRelativeTimeSpanString(alarm.whenStarted)}"
                    }
                    return@post
                }

                marker.remove()
            }

            Log.d("Alarming", "Raised alarm on journey ${alarm.journey} ${DateUtils.getRelativeTimeSpanString(alarm.whenStarted)}")
            val newMarker = googleMap.addMarker(MarkerOptions()
                .position(LatLng(alarm.step.latitude, alarm.step.longitude))
                .flat(true)
                .visible(true)
                .snippet("${DateUtils.getRelativeTimeSpanString(alarm.whenStarted)}")
                .title("Alarm"))

            alarmedPositions += journey to newMarker
        }
    }

    override fun onAcknowledge(acknowledge: Acknowledge) {
        mainHandler.post {
            Log.d("Alarming", "Acknowledged alarm in journey ${acknowledge.journey}")
            val journey = acknowledge.journey
            val wasAlarmed = alarmedPositions.containsKey(journey)

            Log.d("Alarming", "Journey ${acknowledge.journey} is known: $wasAlarmed")
            if (wasAlarmed) {
                val marker = alarmedPositions[journey]!!
                marker.remove()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val curLatLng = LatLng(location.latitude, location.longitude)

        journey.step(Step(location.altitude, location.longitude, location.latitude))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 15f))

        val points = myJourney!!.points
        points.add(curLatLng)
        myJourney!!.points = points
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}
