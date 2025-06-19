package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uniovi.melhouse.R
import com.uniovi.melhouse.network.InternetConnectionObserver
import com.uniovi.melhouse.preference.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

abstract class AbstractActivity : AppCompatActivity() {

    private var isDialogShowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InternetConnectionObserver
            .instance(this)
            .setOnDisconnectHandler {
                checkReallyDisconnect()
            }
            .register()

        onCreateCallback()
        startPingServer()
    }

    // Template method: onCreate can be overriden without calling super
    open fun onCreateCallback() {
        if (!InternetConnectionObserver.hasConnection()) {
            showConnectionLostDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        InternetConnectionObserver
            .unRegister()
    }

    private fun checkReallyDisconnect() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1500)
            if (!InternetConnectionObserver.hasConnection()) {
                showConnectionLostDialog()
            }
        }
    }

    private fun startPingServer() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                delay(10000) // 10 segundos
                pingServer()
            }
        }
    }

    private fun pingServer() {
        val url = URL(Config.SUPABASE_URL + "/rest/v1/")
        try {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("apikey", Config.SUPABASE_ANON_KEY)
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            val responseCode = connection.responseCode
            if (responseCode != 200) {
                lifecycleScope.launch(Dispatchers.Main) {
                    showConnectionLostDialog()
                }
            }
            connection.disconnect()
        } catch (e: Exception) {
            lifecycleScope.launch(Dispatchers.Main) {
                showConnectionLostDialog()
            }
        }
        if (!InternetConnectionObserver.hasConnection()) {
            lifecycleScope.launch(Dispatchers.Main) {
                showConnectionLostDialog()
            }
        }
    }

    protected fun showConnectionLostDialog() {
        if (isDialogShowed) return
        isDialogShowed = true
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.error_lost_connection_title))
            .setMessage(getString(R.string.error_lost_connection_msg))
            .setPositiveButton(getString(R.string.error_lost_connection_btn)) { _, _ ->
                isDialogShowed = false
                restartApp()
            }
            .setCancelable(false)
            .show()
    }

    private fun restartApp() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        exitProcess(0)
    }
}