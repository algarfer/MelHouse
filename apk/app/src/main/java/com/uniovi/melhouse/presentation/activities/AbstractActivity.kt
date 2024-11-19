package com.uniovi.melhouse.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uniovi.melhouse.R
import com.uniovi.melhouse.network.InternetConnectionObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

abstract class AbstractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InternetConnectionObserver
            .instance(this)
            .setOnDisconnectHandler {
                checkReallyDisconnect()
            }
            .register()

        onCreateCallback()
    }

    // Template method: onCreate can be overriden without calling super
    open fun onCreateCallback() {
        if(!InternetConnectionObserver.hasConnection()) {
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
            if(!InternetConnectionObserver.hasConnection()) {
                showConnectionLostDialog()
            }
        }
    }

    protected fun showConnectionLostDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.error_lost_connection_title))
            .setMessage(getString(R.string.error_lost_connection_msg))
            .setPositiveButton(getString(R.string.error_lost_connection_btn)) { _, _ ->
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