package com.example.qmsapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import com.example.qmsapp.databinding.ActivityMainBinding
import com.example.qmsapp.models.NotificationViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var actionBar: ActionBar? = null
    public var token:String?=null
    //private val scope = CoroutineScope(getAccessToken())
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        actionBar = supportActionBar

        binding.bottomNavigation.setOnItemSelectedListener { item->
           when (item.itemId)
           {
               R.id.item_home->{
                   findNavController(R.id.main_fragment_container).navigate(R.id.homeFragment)
                   true
               }
               R.id.item_account->{
                   findNavController(R.id.main_fragment_container).navigate(R.id.accountFragment)
                   true
               }
               R.id.item_tickets->{
                   findNavController(R.id.main_fragment_container).navigate(R.id.myTicketsFragment)
                   true
               }
               else->false
           }
       }



        if (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            }
            else {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
            }

        askNotificationPermission()
        }

    public fun setActionBarTitle(title:String,hasBackButton:Boolean)
    {
        if(actionBar!=null)
        {
            actionBar!!.title=title

            actionBar!!.setDisplayHomeAsUpEnabled(hasBackButton)

        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {
                Toast.makeText(applicationContext, "Podaci o lokaciji korisnika nisu dostupi", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onSupportNavigateUp(): Boolean {
        //Toast.makeText(applicationContext, "nazad", Toast.LENGTH_SHORT).show()
        return findNavController(R.id.main_fragment_container).popBackStack()
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.main_fragment_container))
                || super.onOptionsItemSelected(item)
    }*/

    override fun onBackPressed() {
        if(true)
            return
        super.onBackPressed()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("msg", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    token = task.result
                    Log.d("token",token!!)
                    //sendFirebaseNotification(token)

                })
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    /*fun sendFirebaseNotification(token:String)
    {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val url = "https://fcm.googleapis.com/v1/projects/qmsappauth/messages:send"
                val bodyJson = JSONObject()
                bodyJson.put("message",
                    JSONObject().also {
                        it.put("token", token)
                        it.put("notification", JSONObject().also {
                            it.put("body","BODY")
                            it.put("title","TITLE")
                        })
                    }
                )

                val accessToken=getAccessToken()

                Log.d("token",accessToken!!)


                val request = Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer ${accessToken}")
                    .post(
                        bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                    )
                    .build()

                val client = OkHttpClient()

                client.newCall(request).enqueue(
                    object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            if(response.isSuccessful)
                                Log.d("token","Message sent!")
                            else
                                Log.d("token",response.toString())
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("token",e.message.toString())
                        }
                    }
                )

            } catch (e: Exception) {
                Log.d("token",e.toString())
            }

        }



    }

    @Throws(IOException::class)
    private suspend fun getAccessToken(): String? {
        return withContext(Dispatchers.IO) {
            val fileNameWithPath = "firebase_credentials"
            val inputStream = baseContext!!.assets.open("firebase_credentials.json")
            val credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"))

            // Authenticate and refresh the credentials to obtain an access token
            credentials.refresh()

            // Return the access token
            credentials.accessToken.tokenValue
        }
       /* val jasonfile = getResources().openRawResource(getApplicationContext().getResources().getIdentifier("firebase_credentials","raw",getPackageName()));
        val googleCredentials: GoogleCredentials = GoogleCredentials
            .fromStream(jasonfile)
            .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"))
        googleCredentials.refresh()
        return googleCredentials.accessToken.tokenValue*/
    }*/
    
}