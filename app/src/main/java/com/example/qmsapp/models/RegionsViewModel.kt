package com.example.qmsapp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qmsapp.data.Region
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class RegionsViewModel:ViewModel() {
    val _regions: MutableLiveData<ArrayList<Region>> = MutableLiveData<ArrayList<Region>>()
    val regions: LiveData<ArrayList<Region>> get() = _regions
    val regionList:ArrayList<Region> = ArrayList<Region>()
    val serviceIp:String = "https://qmsdemo.infotech.rs/qms_web/MobileQueue/"

    fun getAllRegions(callback: (ArrayList<Region>?) -> Unit) {

        regionList.clear()
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

        val sslContext=SSLContext.getInstance("SSL")

        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.getSocketFactory();

        val builder=OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory,trustAllCerts[0] as X509TrustManager)

        builder.hostnameVerifier(object : HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                return true
            }

        })

        val client =builder.build()
        val url:String="${serviceIp}GetRegions"
        val request=Request.Builder().url(url).build()

        //poziv getRegions
        client.newCall(request).enqueue(object :Callback{

            override fun onFailure(call: Call, e: IOException) {
                Log.d("cao",e.toString())
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    val string="{regions:${response.body!!.string()}}"
                    val json =JSONObject(string)

                    val jsonArray=json.getJSONArray("regions")

                    for (i in 0 until jsonArray.length())
                    {
                        val region:Region=Gson().fromJson(jsonArray.getJSONObject(i).toString(),Region::class.java)
                        regionList.add(region)

                        Log.d("cao",region.toString())
                    }
                    _regions.postValue(regionList)
                    callback(regionList)
                }
                else
                {
                    Log.d("cao","nije succesfull")
                    callback(null)
                }
            }

        })
    }

    fun getIcon(regionId:Int,callback: (Bitmap?) -> Unit)
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

        val sslContext=SSLContext.getInstance("SSL")

        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.getSocketFactory();

        val builder=OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory,trustAllCerts[0] as X509TrustManager)

        builder.hostnameVerifier(object : HostnameVerifier {
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                return true
            }

        })

        val client =builder.build()
        val url:String="${serviceIp}GetResources/RegionIcon/${regionId}"

        val request=Request.Builder().url(url).build()

        client.newCall(request).enqueue(object :Callback{

            override fun onFailure(call: Call, e: IOException) {
                Log.d("icon",e.toString())
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    val iconBytes:ByteArray=response.body!!.bytes()
                    val icon:Bitmap= BitmapFactory.decodeByteArray(iconBytes,0,iconBytes.size)
                    Log.d("icon",icon.toString())
                    callback(icon)

                }
                else
                {
                    Log.d("icon","nije succesfull")
                    callback(null)
                }
            }

        })

    }

}