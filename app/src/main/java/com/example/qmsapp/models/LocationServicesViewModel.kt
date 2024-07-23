package com.example.qmsapp.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qmsapp.data.Location
import com.example.qmsapp.data.LocationServices
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class LocationServicesViewModel:ViewModel() {

    val _services: MutableLiveData<ArrayList<LocationServices>> = MutableLiveData<ArrayList<LocationServices>>()
    val services: LiveData<ArrayList<LocationServices>> get() = _services
    private var servicesList: ArrayList<LocationServices> = ArrayList<LocationServices>()
    val serviceIp:String = "https://qmsdemo.infotech.rs/qms_web/MobileQueue/"

    fun getAllServices( locationId:Int,callback: (ArrayList<LocationServices>?)->Unit)
    {
        servicesList.clear()
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf<X509Certificate>()
                }

            }
        )

        val sslContext= SSLContext.getInstance("SSL")

        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.getSocketFactory();

        val builder= OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory,trustAllCerts[0] as X509TrustManager)

        builder.hostnameVerifier(object : HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                return true
            }

        })

        val client =builder.build()
        val url:String="${serviceIp}GetServices/${locationId}"
        val request= Request.Builder().url(url).build()


        //poziv getServices
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("cao","exception")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                   try {
                       val string = "{services:${response.body!!.string()}}"
                       val json = JSONObject(string)

                       val jsonArray = json.getJSONArray("services")
                       Log.d("limitations", jsonArray.toString())

                       for (i in 0 until jsonArray.length()) {
                           Log.d("limitations", jsonArray.getJSONObject(i).toString())
                           val service: LocationServices =
                               Gson().fromJson(
                                   jsonArray.getJSONObject(i).toString(),
                                   LocationServices::class.java
                               )
                           servicesList.add(service)
                       }
                   }
                   catch (ex:Exception) {
                       Log.d("limitations", ex.toString())
                       callback(null)
                   }
                    _services.postValue(servicesList)
                    callback(servicesList)
                }
                else
                {
                    Log.d("cao","nije succesfull")
                    callback(null)
                }
            }

        })
    }

    fun getAverageServiceWaitTime(serviceId:Int,callback: (String?) -> Unit)
    {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf<X509Certificate>()
                }

            }
        )

        val sslContext= SSLContext.getInstance("SSL")

        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.getSocketFactory();

        val builder= OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory,trustAllCerts[0] as X509TrustManager)

        builder.hostnameVerifier(object : HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                return true
            }

        })

        val client =builder.build()
        val url:String="${serviceIp}GetWaitingTimeForService?serviceId=${serviceId}"
        val request= Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("cao","exception")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    try {
                        val time=response.body!!.string()
                        val newTime=time.removePrefix("\"").removeSuffix("\"")
                        Log.d("waitingTime",newTime)
                        callback(newTime)
                    }
                    catch (ex:Exception) {
                        Log.d("greska", ex.toString())
                        callback(null)
                    }
                }
                else
                {
                    Log.d("cao","nije succesfull")
                    callback(null)
                }
            }

        })
    }
}