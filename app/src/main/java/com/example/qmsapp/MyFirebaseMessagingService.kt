package com.example.qmsapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.UUID

class MyFirebaseMessagingService:FirebaseMessagingService() {

    private var token:String?=null
    override fun onNewToken(token: String) {
            super.onNewToken(token)
            /*this.token=token
        Firebase.messaging.subscribeToTopic(token)
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("token", msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }*/
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("token","primio")
        showNotification(message)
        
    }

    fun showNotification(message: RemoteMessage)
    {
        val notification= message.notification
        if(notification!=null) {
            val intent: Intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val defaultSoundUri: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            /*val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,"quickq_channel")
                .setContentTitle(notification!!.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notif_icon)*/

            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            createNotificationChannel()

            val notificationCompat= NotificationManagerCompat.from(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,"quickq_channel")
                    .setContentTitle(notification!!.title)
                    .setContentText(notification.body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.notif_icon)
                notificationCompat.notify(1234,notificationBuilder.build())
            }
            else
            {
                val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,"quickq_channel")
                    .setContentTitle(notification!!.title)
                    .setContentText(notification.body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.notif_icon)
                notificationCompat.notify(1234,notificationBuilder.build())
            }


        }
        else{
            Toast.makeText(this, "null notif", Toast.LENGTH_SHORT).show()
        }
    }
    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if(notificationManager!=null && notificationManager.getNotificationChannel("quickq_channel")==null)
            {
                val notificationChannel=NotificationChannel("quickq_channel","Quick q notificationChannel",NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.description="This is a quickq apps notification channel"
                notificationChannel.enableLights(true)
                notificationChannel.lightColor=R.color.soft_red_700
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }

    /*fun sendMessage(myToken:String){
        val message=RemoteMessage.Builder(myToken).addData("msg","TEST MESSAGE").build()
        FirebaseMessaging.getInstance().send(message)

    }
     fun sendNotification(remoteMessage:RemoteMessage?,t:String,context: Context) {
        /*val intent = Intent(this, MainActivity::class.java)
         val ma=MainActivity()
        val pendingIntent =
            PendingIntent.getActivity(ma.application, 100, intent, PendingIntent.FLAG_ONE_SHOT)*/
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, t)
            .setContentTitle("cao")
            .setContentText("cao")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            //.setContentIntent(pendingIntent);
        val notificationManager:NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

// Since android Oreo notification channel is needed.
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                t,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }*/
        notificationManager.notify(100, notificationBuilder.build());
    }*/
}