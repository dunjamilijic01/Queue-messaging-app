package com.example.qmsapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.qmsapp.data.Location
import com.example.qmsapp.models.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.play.integrity.internal.ac
import com.google.android.play.integrity.internal.al
import com.google.android.play.integrity.internal.o

class RecyclerViewAdapter(private val context:Context,private val dataSet:ArrayList<Location>, /*private val fragmentManager: FragmentManager*/ val navController:NavController,locationViewModel: LocationViewModel):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val locationManager:LocationManager = LocationManager(context)
    private val locationVm=locationViewModel

    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val textViewName:TextView
        val textViewAddress:TextView
        val textViewDistance:TextView
        val textViewQueueSize:TextView
        val context: Context
        init {
            textViewName=view.findViewById(R.id.locationNameTextView)
            textViewAddress=view.findViewById(R.id.itemAddres)
            textViewDistance=view.findViewById(R.id.distanceValueTextView)
            textViewQueueSize=view.findViewById(R.id.queueSize)
            context=view.context
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        val location:Location=dataSet[position]
        holder.textViewName.text=location.name
        holder.textViewAddress.text="${location.address}, ${location.city}"
        holder.textViewName.paintFlags=0


        if(location.lat!=null && location.lng!=null) {
            locationManager.findLocation { loc ->
                if (loc != null) {

                   /* val distance: FloatArray = FloatArray(2)
                    android.location.Location.distanceBetween(
                        loc.latitude,
                        loc.longitude,
                        location.lat.toDouble(),
                        location.lng.toDouble(),
                        distance
                    )
                    val distance2dig: Double = String.format("%.2f", distance[0] / 1000).toDouble()*/
                    holder.textViewDistance.text = "${locationVm.getDistance(loc.latitude,loc.longitude,location.lat.toDouble(),location.lng.toDouble())} km"
                } else {

                }
            }
        }

        locationVm.getClientsForLocation(location.locId){numberOfClients->
            if(numberOfClients!=null)
            {
                val mainHandler = Handler(context.getMainLooper());

                val myRunnable = object :Runnable{
                    override fun run() {
                        holder.textViewQueueSize.text=numberOfClients.toString()
                    }

                }
                mainHandler.post(myRunnable);

            }
            else
            {

            }

        }

        holder.itemView.setOnClickListener {

            val bundle= Bundle()
            bundle.putInt("id",location.locId)
            bundle.putString("address",location.address)
            bundle.putString("opensAt",location.opensAt.toString())
            bundle.putString("closesAt",location.closesAt.toString())
            bundle.putString("name",location.name)
            if(location.lat!=null)
                bundle.putFloat("lat",location.lat)
            if(location.lng!=null)
                bundle.putFloat("lng",location.lng)

            val placeFragment=PlaceFragment()
            placeFragment.arguments=bundle

            navController.navigate(R.id.action_categoryFragment_to_placeFragment,bundle)
            //fragmentManager.beginTransaction().replace(R.id.main_fragment_container,placeFragment).addToBackStack(null).commit()
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }



}