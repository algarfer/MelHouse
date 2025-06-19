package com.uniovi.melhouse.services

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uniovi.melhouse.R
import com.uniovi.melhouse.data.SupabaseUserSessionFacade
import com.uniovi.melhouse.preference.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var prefs: Prefs

    @Inject
    lateinit var supabaseUserSessionFacade: SupabaseUserSessionFacade

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        prefs.setFcmToken(token)
        prefs.setFcmTokenStoredServer(false)

        supabaseUserSessionFacade.getUserId()?.let {
            val job = SupervisorJob()
            val scope = CoroutineScope(Dispatchers.IO + job)

            val handler = Handler(Looper.getMainLooper())
            val timeout = 10000L // 10 secs

            scope.launch {
                try {
                    supabaseUserSessionFacade.updateFCMToken(token)
                } catch (e: CancellationException) {
                }
            }

            handler.postDelayed({
                job.cancel()
            }, timeout)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notification = message.getNotification()
        if (notification != null) {
            val title: String = notification.title!!
            val body: String = notification.body!!
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String, body: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, "default_channel")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val manager = NotificationManagerCompat.from(this)
            manager.notify(Random.nextInt(), builder.build())
        }
    }
}