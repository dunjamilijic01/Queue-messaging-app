package com.example.qmsapp.models

import android.util.Log
import android.widget.Toast
import androidx.core.widget.EdgeEffectCompat
import androidx.core.widget.EdgeEffectCompat.getDistance
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qmsapp.data.Location
import com.example.qmsapp.data.Region
import com.google.android.play.integrity.internal.i
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class LocationViewModel :ViewModel() {
    val _locations: MutableLiveData<ArrayList<Location>> = MutableLiveData<ArrayList<Location>>()
    val locations: LiveData<ArrayList<Location>> get() = _locations
    var locationList:ArrayList<Location> = ArrayList<Location>()
    val _filterRadius:MutableLiveData<Float> = MutableLiveData<Float>()
    val filterRadius:LiveData<Float> get() =_filterRadius
    val serviceIp:String = "https://qmsdemo.infotech.rs/qms_web/MobileQueue/"

    fun getAllLocations (regionId:Int,callback: (ArrayList<Location>?) -> Unit)
    {

        locationList.clear()
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
        val url:String="${serviceIp}GetLocations/${regionId}"
        val request= Request.Builder().url(url).build()

        //poziv getLocations
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("cao","exception")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    val string="{locations:${response.body!!.string()}}"
                    val json=JSONObject(string)

                    val jsonArray=json.getJSONArray("locations")
                    Log.d("Cao",jsonArray.toString())

                    for(i in 0 until jsonArray.length())
                    {
                        val location:Location=Gson().fromJson(jsonArray.getJSONObject(i).toString(),Location::class.java)
                        locationList.add(location)
                        Log.d("Cao",location.toString())
                    }
                    _locations.postValue(locationList)
                    callback(locationList)

                }
                else
                {
                    Log.d("cao","nije succesfull")
                    callback(null)
                }
            }

        })
    }
    fun getLocationName( locationId:Int,callback: (String?)->Unit)
    {
        Log.d("greska","Uso u poziv fje location name")
        val list=ArrayList<Location>()
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
        val url:String="${serviceIp}/Booking/Locations"
        val request= Request.Builder().url(url).build()

        //poziv getLocations
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("greska","exception")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("greska","Uso u poziv servisa za name")
                if(response.isSuccessful)
                {
                    try {

                        val string = "{locations:${response.body!!.string()}}"
                        val json = JSONObject(string)

                        val jsonArray = json.getJSONArray("locations")

                        for (i in 0 until jsonArray.length()) {
                            Log.d("greska",jsonArray.getJSONObject(i).getInt("locationId").toString())
                            if (jsonArray.getJSONObject(i).getInt("locationId") == locationId)
                                callback(jsonArray.getJSONObject(i).getString("locationName"))

                            /*val location:Location=Gson().fromJson(jsonArray.getJSONObject(i).toString(),Location::class.java)
                        locationList.add(location)
                        Log.d("Cao",location.toString())*/

                        }
                        callback("")
                        //_locations.postValue(locationList)
                    }
                    catch (e:Exception)
                    {
                        Log.d("greska",e.toString())
                    }

                }
                else
                {
                    callback("")

                    Log.d("greska","GRESKA")
                }
            }

        })
    }

    fun getDistance(lat1:Double,lng1:Double,lat2:Double,lng2:Double) :Double{

        val distance: FloatArray = FloatArray(2)
        android.location.Location.distanceBetween(
            lat1,
            lng1,
            lat2,
            lng2,
            distance
        )
        val distance2dig: Double = String.format("%.2f", distance[0] / 1000).toDouble()
        return  distance2dig
    }

    fun getFilteredLocations(distance:Float,myLat:Double,myLng:Double):ArrayList<Location>
    {
        var filteredLocations:ArrayList<Location> = ArrayList<Location>()
        locations.value?.let {
           it.forEach {
               if (it.lat != null && it.lng != null) {
                   if (getDistance(myLat, myLng, it.lat.toDouble(), it.lng.toDouble()) < distance) {
                       filteredLocations.add(it)
                   }
               }
           }

       }
        return filteredLocations
    }

    fun getLocationForService(serviceId:Int,callback:(Location?)->Unit){
        Log.d("greska","uso u reserved")

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

        val sslContext = SSLContext.getInstance("SSL")

        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.getSocketFactory();

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier(object : HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                return true
            }

        })

        val client = builder.build()
        val url: String = "${serviceIp}GetLocationsForService/${serviceId}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("greska",e.toString())
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Log.d("greska","uso")
                    try {
                        /*var resp=response.body!!.string()
                        resp.removePrefix("[").removeSuffix("]")
                        Log.d("greska",resp)*/
                        val string = "{locations:${response.body!!.string()}}"
                        val json = JSONObject(string)
                        val jsonArray = json.getJSONArray("locations")

                        val loc: Location =
                            Gson().fromJson(
                                jsonArray.getJSONObject(0).toString(),
                                Location::class.java
                            )
                        callback(loc)

                    }
                    catch (e:Exception)
                    {
                        Log.d("greska",e.message.toString())
                        callback(null)
                    }
                }
                else
                {
                    Log.d("greska",response.message)
                    callback(null)
                }
            }

        })
    }
    fun getClientsForLocation(locationId: Int,callback: (String?) -> Unit){

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

        val sslContext = SSLContext.getInstance("SSL")

        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.getSocketFactory();

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier(object : HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                return true
            }

        })

        val client = builder.build()
        val url: String = "${serviceIp}GetClientsInFrontOfMe?serviceId=${locationId}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("greska",e.toString())
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Log.d("clients",url)
                    try {
                        callback(response.body!!.string())

                    }
                    catch (e:Exception)
                    {
                        Log.d("clientsE",e.message.toString())
                        callback(null)
                    }
                }
                else
                {
                    Log.d("clients",response.message)
                    callback(null)
                }
            }

        })

    }
}