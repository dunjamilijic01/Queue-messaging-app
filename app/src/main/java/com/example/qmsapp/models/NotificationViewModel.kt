package com.example.qmsapp.models

import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class NotificationViewModel:ViewModel() {
    val serviceIp:String = "https://qmsdemo.infotech.rs/qms_web/MobileQueue/"
    fun subscribe (email:String,token:String,callback: (String?) -> Unit)
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

        val emailReplaced=email.replace("@","_").replace(".","_")
        val payload = "nesto"
        Log.d("notif",emailReplaced)
        Log.d("notif",token)
       /* val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", token).addFormDataPart("topic",emailReplaced)
            .build()*/
        val json ="{\n" +
                "  \"token\": \"d7PcWbpGTmSP-26kv_DPqo:APA91bEPZSFAqIZcIlmDsE8AuwVMgJb8lihcHnxH7g5Dtd0HFDohU4q4pRuaLdz2OUHG0pm_67uv2BQDO4G_bpkCKpQwctu4L4HiLEmMbJbwa8fXSgLiUSUi6mZMDVz6SevOt-iH5jnC\",\n" +
                "  \"topic\": \"proba_gmail_com\"\n" +
                "}"

        val body: RequestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(), json)
        //val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json)
        //val requestBody = payload.toRequestBody()
        val client =builder.build()
        val url:String="${serviceIp}FCMSubscribe"
        val request= Request.Builder().post(body).url(url).build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("cao","exception")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful)
                {
                    try {

                        val json = JSONObject(response.body!!.string())
                        val success = json.getInt("successCount")
                        //val failure = json.getJSONObject("failureCount")
                        if (success==1)
                            callback("Uspesno")
                        else
                            callback("Neuspesno")
                    }
                    catch (e:Exception)
                    {
                        Log.d("notif",e.message!!)
                    }

                }
                else
                {
                   callback("GRESKA")

                }
            }

        })
    }
    fun sanitizeTopic(topic:String):String {
        // Replace characters not allowed in topic names with underscores
        var sanitizedTopic = topic.replace(Regex("/[^a-zA-Z0-9_\\-]/g"),"_")

        // Ensure the topic name doesn't exceed 200 characters
        if (sanitizedTopic.length > 200) {
            sanitizedTopic = sanitizedTopic.substring(0, 200);
        }

        return sanitizedTopic.toLowerCase(); // Convert to lowercase
    }

}