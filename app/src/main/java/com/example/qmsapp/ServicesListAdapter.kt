package com.example.qmsapp

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.qmsapp.data.LocationServices
import com.example.qmsapp.models.LocationServicesViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ServicesListAdapter(private val context: Activity, private val dataSet:ArrayList<LocationServices>,val fragmentManager:FragmentManager,val placeName:String,val locationId:Int,val placeAddresss:String,val distance:Double,val locationServicesViewModel: LocationServicesViewModel)
    :BaseAdapter() {

    private val serviceViewModel:LocationServicesViewModel=locationServicesViewModel
    private var allowedDistance:Double = 50.0
    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(p0: Int): Any {
        return dataSet.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.service_list_view_item, null, true)

        val branchName = view.findViewById<TextView>(R.id.serviceNameTextView)
        val buttonGetInLine= view.findViewById<Button>(R.id.buttonGetInLine)
        val buttonReserve= view.findViewById<Button>(R.id.buttonReserve)
        val service:LocationServices=dataSet[position]
        val locationManager=LocationManager(context)
        val averageWaitingTime= view.findViewById<TextView>(R.id.averageWaitingForService)

        buttonGetInLine.isEnabled =
            !(service.counterServices==null || service.counterServices.size==0)
        buttonReserve.isEnabled =
            !(service.limitations.size==0 || service.limitations[0].appointmentsAllowed!=1)

        buttonReserve.setOnClickListener {
            val bundle= Bundle()
            bundle.putInt("id",service.serviceId)
            bundle.putString("title",service.serviceName)
            bundle.putString("locationName",placeName)
            val parent=context as MainActivity

            val intent: Intent =Intent(context,ReservationActivity::class.java)
            intent.putExtra("id",service.serviceId.toString())
            intent.putExtra("title",service.serviceName)
            intent.putExtra("locationName",placeName)
            intent.putExtra("token",parent.token)

            context.startActivity(intent)

           /* val reserveFragment=ReserveFragment()
            reserveFragment.arguments=bundle*/

            //fragmentManager.beginTransaction().replace(R.id.main_fragment_container,reserveFragment).addToBackStack(null).commit()
        }



        buttonGetInLine.setOnClickListener {
            if(distance<=allowedDistance)
            {
                //Toast.makeText(context, distance.toString(), Toast.LENGTH_SHORT).show()
                val bundle= Bundle()
                bundle.putInt("id",service.serviceId)
                bundle.putString("title",service.serviceName)
                bundle.putString("locationName",placeName)
                bundle.putString("locationId",locationId.toString())

                val intent: Intent =Intent(context,GetInLineActivity::class.java)
                intent.putExtra("id",service.serviceId.toString())
                intent.putExtra("title",service.serviceName)
                intent.putExtra("locationName",placeName)
                intent.putExtra("locationAddress",placeAddresss)
                intent.putExtra("locationId",locationId.toString())

                context.startActivity(intent)
            }
            else
            {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Odbijeno")
                builder.setMessage("Da biste stali u red morate biti u radijusu od 500 metara od zeljenog objekta!")
                builder.setPositiveButton("OK",object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                    }

                })

                builder.show()
            }

        }

        serviceViewModel.getAverageServiceWaitTime(dataSet[position].serviceId){time->
            if(time!=null)
            {
                try {
                    val localTime = LocalTime.parse(time)
                    val mainHandler = Handler(context.getMainLooper());

                    val myRunnable = object :Runnable{
                        override fun run() {
                            averageWaitingTime.text="~${localTime.minute} min"
                        }

                    }
                    mainHandler.post(myRunnable)
                }
                catch (e:Exception)
                {
                    Log.d("greska",e.message.toString())
                }
            }
        }

        branchName.text = service.serviceName
        return view
    }
}