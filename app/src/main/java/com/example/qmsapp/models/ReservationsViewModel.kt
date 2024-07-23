package com.example.qmsapp.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.qmsapp.data.Region
import com.example.qmsapp.data.Reservation
import com.google.android.play.integrity.internal.i
import com.google.gson.Gson
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*
import kotlin.collections.ArrayList

class ReservationsViewModel:ViewModel() {
    private var availableDates:ArrayList<String> = ArrayList<String>()
    private var availableAppointments:ArrayList<String> = ArrayList<String>()
    val serviceIp:String = "https://qmsdemo.infotech.rs/qms_web/"


    fun getAvailableDates(serviceId:Int,callback: (ArrayList<String>?) -> Unit) {
        availableDates.clear()

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
        val url: String = "${serviceIp}Booking/AvailableDates/${serviceId}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("dates",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                val string="{dates:${response.body!!.string()}}"
                val json = JSONObject(string)
                val jsonArray=json.getJSONArray("dates")
                for (i in 0 until jsonArray.length())
                {
                   /* val date: String =
                        Gson().fromJson(jsonArray.getJSONObject(i).toString(), String::class.java)*/
                    //Log.d("dates",jsonArray.getString(i))
                    availableDates.add(jsonArray.getString(i))
                }
                callback(availableDates)
            }
                else
                {
                    callback(null)
                }
            }

        })
    }

    fun getAvailableAppointments(date: String, serviceId:Int,callback: (ArrayList<String>?) -> Unit)
    {
        availableAppointments.clear()
        Log.d("appointments",serviceId.toString())

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
        val url: String = "${serviceIp}Booking/TimeSlots/${serviceId}?date=${date}"
        Log.d("appointments",url)
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("appointments",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val string="{appointments:${response.body!!.string()}}"
                    val json = JSONObject(string)
                    val jsonArray=json.getJSONArray("appointments")
                    for (i in 0 until jsonArray.length())
                    {
                        availableAppointments.add(jsonArray.getString(i))
                    }
                    callback(availableAppointments)
                }
                else
                {
                    Log.d("appointments",response.message)
                    callback(null)
                }
            }

        })

    }
    fun getETicket(serviceId:Int,email:String,callback:(Reservation?)->Unit){

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

        val payload = "test payload"
        val requestBody = payload.toRequestBody()
        val client = builder.build()
        val url: String = "${serviceIp}MobileQueue/GetETicket?serviceId=${serviceId}&dispenserId=1&clientEmail="+URLEncoder.encode(email,"utf-8")
        val request = Request.Builder().post(requestBody).url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("eTicketFail",e.toString())
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                   try {
                       val string = "{eTicket:${response.body!!.string()}}"
                       val json = JSONObject(string)
                       val jsonArray = json.getJSONArray("eTicket")
                       val eTicket = Gson().fromJson(
                           jsonArray.getJSONObject(0).toString(),
                           Reservation::class.java
                       )

                       callback(eTicket)
                       Log.d("eTicketSucc",response.body!!.string())
                       //callback(null)
                   }
                   catch (e:Exception)
                   {
                       Log.d("eTicketEx",e.toString())
                       callback(null)
                   }
                }
                else
                {
                    Log.d("eTicketErr",response.message)
                    callback(null)
                    //callback(null)
                }
            }

        })
    }
    fun makeAReservation(serviceId: Int,email: String,dateTime:String,callback: (Pair<Reservation?,String?>?) -> Unit)
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

        val payload = "test payload"
        val requestBody = payload.toRequestBody()
        val client = builder.build()
        val url: String = "${serviceIp}MobileQueue/CreateAppointment?serviceId=${serviceId}&appointmentDateTime=${dateTime}&clientEmail="+URLEncoder.encode(email,"utf-8")
        val request = Request.Builder().post(requestBody).url(url).build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("reserveFail",e.toString())
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    try {
                        val string = "{reservation:${response.body!!.string()}}"
                        val json = JSONObject(string)
                        val jsonArray = json.getJSONObject("reservation")
                        val eTicket = Gson().fromJson(
                            jsonArray.toString(),
                            Reservation::class.java
                        )

                        callback(Pair(eTicket,null))
                        Log.d("reservationSucc",response.body!!.string())
                        //callback(null)
                    }
                    catch (e:Exception)
                    {
                        Log.d("reservationEx",e.toString())
                        callback(null)
                    }
                }
                else
                {
                    val message=response.body!!.string()
                    Log.d("reservationErr",message)
                    callback(Pair(null,message))
                }
            }

        })
    }

    fun cancelReservation(email:String,ticketId:Int,callback: (String?) -> Unit){

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

        Log.d("deleteErr",email+" "+ticketId)
        val payload = "test payload"
        val requestBody = payload.toRequestBody()
        val client = builder.build()
        val url: String = "https://qmsdemo.infotech.rs/qms_web/MobileQueue/CancelTicket?clientEmail="+URLEncoder.encode(email,"utf-8")+"&ticketId="+ticketId
        val request = Request.Builder().delete(requestBody).url(url).build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("deleteFail",e.toString())
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    try {
                        /*val string = "{reservation:${response.body!!.string()}}"
                        val json = JSONObject(string)
                        val jsonArray = json.getJSONObject("reservation")
                        val eTicket = Gson().fromJson(
                            jsonArray.toString(),
                            Reservation::class.java
                        )*/

                        callback("Uspesno ste otkazali tiket!")
                        Log.d("deleteSucc","Uspesno ste otkazali tiket!")
                        //callback(null)
                    }
                    catch (e:Exception)
                    {
                        Log.d("deleteEx",e.toString())
                        callback(null)
                    }
                }
                else
                {
                    val message=response.body!!.string()
                    Log.d("deleteErr",message)
                    callback(message)
                }
            }

        })

    }
}