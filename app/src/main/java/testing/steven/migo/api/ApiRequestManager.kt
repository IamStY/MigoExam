package testing.steven.migo.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import testing.steven.migo.MigoConstants
import testing.steven.migo.datamodel.MigoResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object ApiRequestManager {
    var volleyRequestQueue: RequestQueue? = null
    private suspend fun <T> getData(context: Context, directFetchPublic: Boolean): T = suspendCoroutine { cont ->

        val requestPath = if (directFetchPublic.not() && hasWifiConnectivity(context)) {
            MigoConstants.PRIVATE_API_PATH
        } else {
            MigoConstants.PUBLIC_API_PATH
        }
        val stringRequest = StringRequest(
                Request.Method.GET, requestPath,
                { response ->
                    val responseString: String = if (directFetchPublic) {
                        "WIFI Failed : request to Public API: $response"
                    } else {
                        "Cellular : request to Public API: $response"
                    }
                    cont.resume(MigoResponse(responseString = responseString) as T)
                },
                {
                    cont.resume(MigoResponse(exception = it) as T)
                })

        getVolleyRequestQueue(context).add(stringRequest)
    }

    private fun hasWifiConnectivity(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        networkCapabilities ?: return false
        val req = NetworkRequest.Builder()
        val hasTransportWifi = networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
        )
        if (hasTransportWifi) {
            req.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            cm.requestNetwork(req.build(), object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Log.e("wifiAvailable", "true")

                    cm.bindProcessToNetwork(network)
                }
            })
        }
        return hasTransportWifi
    }

    private fun getVolleyRequestQueue(context: Context): RequestQueue {
        if (volleyRequestQueue == null) {
            volleyRequestQueue = Volley.newRequestQueue(context)
        }
        return volleyRequestQueue!!
    }

    suspend fun fetchServerData(
            context: Context,
            directFetchPublic: Boolean = false
    ): MigoResponse {
        return getData(context, directFetchPublic)
    }

}
