package com.uniovi.melhouse.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.uniovi.melhouse.preference.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

// Source: https://medium.com/@rawatsumit115/smart-way-to-observe-internet-connection-for-whole-app-in-android-kotlin-bd77361c76fb
object InternetConnectionObserver {
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var cm: ConnectivityManager? = null
    private val validNetworks: MutableSet<Network> = HashSet()
    private var onConnectHandler: () -> Unit = {}
    private var onDisconnectHandler: () -> Unit = {}

    fun instance(context: Context) : InternetConnectionObserver {
        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return this
    }

    fun setOnConnectHandler(handler: () -> Unit): InternetConnectionObserver {
        this.onConnectHandler = handler
        return this
    }

    fun setOnDisconnectHandler(handler: () -> Unit): InternetConnectionObserver {
        this.onDisconnectHandler = handler
        return this
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        /*
          Called when a network is detected. If that network has internet, save it in the Set.
          Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onAvailable(android.net.Network)
         */
        override fun onAvailable(network: Network) {
            val networkCapabilities = cm?.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (hasInternetCapability == true) {
                // check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = checkInternetConnection(network.socketFactory)
                    if(hasInternet){
                        withContext(Dispatchers.Main) {
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        /*
          If the callback was registered with registerNetworkCallback() it will be called for each network which no longer satisfies the criteria of the callback.
          Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onLost(android.net.Network)
         */
        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }

    }

    private fun checkInternetConnection(socketFactory: SocketFactory): Boolean {
        return try {
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null")
            socket.connect(InetSocketAddress(Config.SUPABASE_HOST, Config.SUPABASE_PORT), 1500)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun checkValidNetworks() {
        val status = validNetworks.size > 0
        if(status){
            onConnectHandler()
        } else {
            onDisconnectHandler()
        }
    }

    fun register(): InternetConnectionObserver {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm?.registerNetworkCallback(networkRequest, networkCallback)
        return this
    }

    fun unRegister() {
        cm?.unregisterNetworkCallback(networkCallback)
    }

    fun hasConnection(): Boolean {
        val network = cm?.activeNetwork
        val capabilities = cm?.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}