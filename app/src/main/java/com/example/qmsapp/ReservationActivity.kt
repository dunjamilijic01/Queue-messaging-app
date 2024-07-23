package com.example.qmsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import com.example.qmsapp.databinding.ActivityMainBinding
import com.example.qmsapp.databinding.ActivityReservationBinding

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    public var serviceTitle:String? =null
    public var serviceId:Int? = null
    public var locarionName:String?=null
    public var token:String?=null
    var actionBar: ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar=supportActionBar
        serviceTitle=intent.getStringExtra("title")
        serviceId=  intent.getStringExtra("id")?.toInt()
        locarionName=intent.getStringExtra("locationName")
        token=intent.getStringExtra("token")
        binding=ActivityReservationBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)


        //binding.reservationHeaderTextView.text="${binding.reservationHeaderTextView.text} ${serviceTitle}"
    }

    override fun onSupportNavigateUp(): Boolean {

            finish()
           return true
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
    public fun setActionBarTitle(title:String,hasBackButton:Boolean)
    {
        if(actionBar!=null)
        {
            actionBar!!.title=title

            actionBar!!.setDisplayHomeAsUpEnabled(hasBackButton)

        }
    }
}