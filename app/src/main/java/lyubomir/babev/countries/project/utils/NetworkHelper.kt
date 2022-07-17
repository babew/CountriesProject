package lyubomir.babev.countries.project.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build

class NetworkHelper(private val context: Context, private val onAvailable: () -> Unit) {

    private val connectionChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onAvailable()
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        private var shouldCheckOnFirstCall = isOnline().not()
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (shouldCheckOnFirstCall)
                onAvailable()
        }
    }

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    @SuppressLint("MissingPermission")
    fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork                 ?: return false
            val actNw   = connectivityManager.getNetworkCapabilities(nw)    ?: return false

            return  actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)      ||
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)  ||
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)  ||
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        } else
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    @SuppressLint("MissingPermission")
    fun startListening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        } else
            context.registerReceiver(connectionChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun stopListening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else
            context.unregisterReceiver(connectionChangeReceiver)
    }
}