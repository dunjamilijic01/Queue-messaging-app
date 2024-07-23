package com.example.qmsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.navigation.findNavController
import com.example.qmsapp.databinding.ActivityGetInLineBinding
import com.example.qmsapp.databinding.ActivityReservationBinding

class GetInLineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetInLineBinding
    public var serviceTitle:String? =null
    public var serviceId:Int? = null
    public var locarionName:String?=null
    public var locationAddress:String?=null
    public var locationId:Int?=null
    var actionBar: ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar = supportActionBar
        /*actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setTitle("Stani u red")*/
        serviceTitle=intent.getStringExtra("title")
        serviceId=  intent.getStringExtra("id")?.toInt()
        locarionName=intent.getStringExtra("locationName")
        locationAddress=intent.getStringExtra("locationAddress")
        locationId=intent.getStringExtra("locationId")?.toInt()
        binding=ActivityGetInLineBinding.inflate(layoutInflater)

        val view=binding.root
        setContentView(view)

    }

    public fun setActionBarTitle(title:String,hasBackButton:Boolean)
    {
        if(actionBar!=null)
        {
            actionBar!!.title=title

            actionBar!!.setDisplayHomeAsUpEnabled(hasBackButton)

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        //st.makeText(applicationContext, "nazad", Toast.LENGTH_SHORT).show()
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
}