package com.example.qmsapp.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qmsapp.data.Location
import com.example.qmsapp.data.Region
import com.example.qmsapp.data.Ticket
import com.google.android.play.integrity.internal.i
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.time.LocalDateTime
import javax.net.ssl.*
import javax.security.auth.callback.Callback

class TicketViewModel:ViewModel() {
    val serviceIp:String = "https://qmsdemo.infotech.rs/qms_web/"
    private var myTickets:ArrayList<Ticket> = ArrayList<Ticket>()
    private val locationViewModel:LocationViewModel= LocationViewModel()
    val _tickets: MutableLiveData<ArrayList<Ticket>> = MutableLiveData<ArrayList<Ticket>>()

    fun getAllTickets(email:String,callback: (ArrayList<Ticket>?)->Unit){
        myTickets.clear()

        Log.d("greska","uso")
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
        val url: String = "${serviceIp}MobileQueue/GetMyTickets?clientEmail=${email}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("tickets",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    try {
                        val string = "{tickets:${response.body!!.string()}}"
                        val json = JSONObject(string)
                        val jsonArray = json.getJSONArray("tickets")
                        for (i in 0 until jsonArray.length()) {
                            //val json2 = JSONObject(string)
                            val jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("serviceTickets")


                            val jsonObject3 = jsonArray2.getJSONObject(0).getJSONObject("fkService")

                            val ticket: Ticket =
                                Gson().fromJson(
                                    jsonArray.getJSONObject(i).toString(),
                                    Ticket::class.java
                                )

                            ticket.label=jsonArray2.getJSONObject(0).getString("label").toString()
                            ticket.serviceName=jsonObject3.getString("serviceName")
                            ticket.serviceId=jsonObject3.getInt("serviceId")



                            /*if(ticket.fkLocId!=null)
                            {
                                locationViewModel.getLocationName(ticket.fkLocId){name->
                                    ticket.locationName=name

                                }

                            }*/


                            myTickets.add(ticket)


                        }

                        if(myTickets.size==0)
                        {
                            val t=Ticket(0,"",null,null,"","",null,null,"NEMA TIKETA",null)
                            myTickets.add(t)
                        }

                        Log.d("test","postTiket")
                        _tickets.postValue(myTickets)
                        callback(myTickets)

                    }
                    catch (e:Exception)
                    {
                        Log.d("test","postPrazno")
                        _tickets.postValue(myTickets)
                        callback(myTickets)
                    }
                }
                else
                {
                    Log.d("test","postPraznoFail")
                    _tickets.postValue(myTickets)
                    callback(myTickets)
                }
            }

        })

    }


    fun getReservedTickets(email:String,callback: (ArrayList<Ticket>?)->Unit){
        myTickets.clear()
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
        val url: String = "${serviceIp}MobileQueue/GetMyTickets?clientEmail=${email}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("greska",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Log.d("greska","uso u poziv fje")
                    try {
                        val string = "{tickets:${response.body!!.string()}}"
                        val json = JSONObject(string)
                        val jsonArray = json.getJSONArray("tickets")
                        for (i in 0 until jsonArray.length()) {
                            //val json2 = JSONObject(string)
                            val jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("serviceTickets")


                            val jsonObject3 = jsonArray2.getJSONObject(0).getJSONObject("fkService")

                            val ticket: Ticket =
                                Gson().fromJson(
                                    jsonArray.getJSONObject(i).toString(),
                                    Ticket::class.java
                                )

                            ticket.label=jsonArray2.getJSONObject(0).getString("label").toString()
                            ticket.serviceName=jsonObject3.getString("serviceName")
                            ticket.serviceId=jsonObject3.getInt("serviceId")

                            /*if(ticket.fkLocId!=null)
                            {
                                locationViewModel.getLocationName(ticket.fkLocId){name->
                                    ticket.locationName=name

                                }

                            }*/
                            if(ticket.dateTimeForService!=null)
                                myTickets.add(ticket)

                            Log.d("t",ticket.toString())


                        }
                        if(myTickets.size==0)
                        {
                            val t=Ticket(0,"",null,null,"","",null,null,"NEMA TIKETA",null)
                            myTickets.add(t)
                        }

                        _tickets.postValue(myTickets)
                        callback(myTickets)

                    }
                    catch (e:Exception)
                    {
                        Log.d("greska",e.message.toString())
                        val t=Ticket(0,"",null,null,"","",null,null,"NEMA TIKETA",null)
                        myTickets.add(t)
                        _tickets.postValue(myTickets)
                        callback(myTickets)
                    }
                }
                else
                {
                    Log.d("greska","nije succesfull")
                    //callback(null)
                    val t=Ticket(0,"",null,null,"","",null,null,"NEMA TIKETA",null)
                    myTickets.add(t)
                    _tickets.postValue(myTickets)
                    callback(myTickets)
                }
            }

        })

    }

    fun getQueuedTickets(email:String,callback: (ArrayList<Ticket>?)->Unit){
        myTickets.clear()

        Log.d("greska","uso u fju")
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
        val url: String = "${serviceIp}MobileQueue/GetMyTickets?clientEmail=${email}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("greska",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Log.d("greska","uso")
                    try {
                        val string = "{tickets:${response.body!!.string()}}"
                        val json = JSONObject(string)
                        val jsonArray = json.getJSONArray("tickets")
                        for (i in 0 until jsonArray.length()) {
                            //val json2 = JSONObject(string)
                            val jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("serviceTickets")


                            val jsonObject3 = jsonArray2.getJSONObject(0).getJSONObject("fkService")

                            val ticket: Ticket =
                                Gson().fromJson(
                                    jsonArray.getJSONObject(i).toString(),
                                    Ticket::class.java
                                )

                            ticket.label=jsonArray2.getJSONObject(0).getString("label").toString()
                            ticket.serviceName=jsonObject3.getString("serviceName")
                            ticket.serviceId=jsonObject3.getInt("serviceId")
                            /*if(ticket.fkLocId!=null)
                            {
                                locationViewModel.getLocationName(ticket.fkLocId){name->
                                    ticket.locationName=name

                                }

                            }*/
                            if(ticket.dateTimeForService==null)
                                myTickets.add(ticket)


                        }
                        if(myTickets.size==0)
                        {
                            val t=Ticket(0,"",null,null,"","",null,null,"NEMA TIKETA",null)
                            myTickets.add(t)
                        }

                        _tickets.postValue(myTickets)
                        callback(myTickets)

                    }
                    catch (e:Exception)
                    {
                        Log.d("greska",e.message.toString())
                        val t=Ticket(0,"",null,null,"","",null,null,"NEMA TIKETA",null)
                        myTickets.add(t)
                        _tickets.postValue(myTickets)
                        callback(myTickets)
                    }
                }
                else
                {
                    Log.d("greska",response.message)
                    val t=Ticket(0,"",null,null,"","",null,null,"NEMA TIKETA",null)
                    myTickets.add(t)
                    _tickets.postValue(myTickets)
                    callback(myTickets)
                }
            }

        })

    }
    fun getClientsInFrontOfMeForService(ticketId:Int,serviceName:String,callback: (Int?)->Unit){

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
        val url: String = "${serviceIp}MobileQueue/GetClientsInFrontOfMeForTicket?ticketId=${ticketId}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("greska",e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Log.d("greska","uso")
                    try {
                        //val string = "{tickets:${response.body!!.string()}}"
                        val json = JSONObject(response.body!!.string())
                        val numberOfClients=json.getInt(serviceName)
                        callback(numberOfClients.toInt())

                    }
                    catch (e:Exception)
                    {
                        Log.d("greska","Ex")
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

}