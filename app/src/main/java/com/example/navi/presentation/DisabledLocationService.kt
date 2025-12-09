package com.example.navi.presentation

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.navi.R
import com.example.navi.cryptoManager.TokenCryptoManager
import com.example.navi.data.locationClient.LocationClientImpl
import com.example.navi.data.remote.dto.LocationDTO
import com.example.navi.data.remote.dto.RefreshTokenDTO
import com.example.navi.di.qualifiers.FilesDir
import com.example.navi.domain.locationClient.LocationClient
import com.example.navi.domain.repository.DisabledRepository
import com.example.navi.domain.util.Resource
import com.example.navi.presentation.locationController.Location
import com.example.navi.presentation.locationController.LocationController
import com.example.navi.presentation.snackBarController.SnackBarController
import com.example.navi.presentation.snackBarController.SnackBarEvent
import com.example.navi.presentation.viewmodels.EXPIRED_TOKEN
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

@AndroidEntryPoint
class DisabledLocationService: Service() {

    @Inject
    lateinit var disabledRepository: DisabledRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClientImpl(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun start() {
        Log.i("DisabledLocationService", "start()")
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("GPSTestProject")
            .setContentText("Tracking location...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .build()
        locationClient
            .getLocationUpdates(5000L)
            .catch { e -> Log.i("locationClient Exception", "${e.message}") }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                Log.i("Location", "$lat, $long")
                val result = disabledRepository.updateLocation(LocationDTO(longitude = long, latitude = lat))
                if(result is Resource.Error) {
                    if(result.responseCode == EXPIRED_TOKEN) {
                        disposeApi()
                    }
                }
                LocationController.updateLocation(Location(lat, long))
            }
            .launchIn(serviceScope)

        ServiceCompat.startForeground(
            this,
            2,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                0
            }
        )
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun disposeApi() {
        val cryptoManager = TokenCryptoManager()
        val refreshToken = cryptoManager.decrypt(
            FileInputStream(File(filesDir, "token.txt"))
        ).refreshToken
        serviceScope.launch {
            when(val result = disabledRepository.refreshToken(RefreshTokenDTO(refreshToken))) {
                is Resource.Success<*> -> {
                    SnackBarController.sendEvent(SnackBarEvent(message = "refreshToken() Success"))
                    disabledRepository.disposeApi(accessToken = result.data?.accessToken!!)
                }
                is Resource.Error<*> -> {
                    // TODO
                }
            }
        }
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}