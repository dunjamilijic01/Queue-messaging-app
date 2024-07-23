package com.example.qmsapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.qmsapp.databinding.FragmentMtTicketsOnMapBinding
import com.example.qmsapp.models.LocationViewModel
import com.example.qmsapp.models.TicketViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth


class MtTicketsOnMap : Fragment() , OnMapReadyCallback {
    private var _binding: FragmentMtTicketsOnMapBinding?=null
    private val binding get()=_binding!!
    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var locationManager:LocationManager
    private val ticketViewModel:TicketViewModel by activityViewModels()
    private val locationViewModel:LocationViewModel by activityViewModels()
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        locationManager= LocationManager(context!!)
        firebaseAuth=FirebaseAuth.getInstance()
        val parent= activity as MainActivity
        parent.setActionBarTitle("",true)
        _binding= FragmentMtTicketsOnMapBinding.inflate(inflater, container, false)

        mapFragment=childFragmentManager.findFragmentById(R.id.google_map_tickets) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val view=binding.root

        return view
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap=p0
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled=true
        }

        locationManager.findLocation { loc->
            if(loc!=null)
            {
                val cameraPosition= CameraPosition(loc,16f,0f,0f)
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

        ticketViewModel.getAllTickets(firebaseAuth.currentUser!!.email!!){tickets ->
            if(tickets!=null)
            {
                tickets.forEach {
                    locationViewModel.getLocationForService(it.serviceId!!){location ->
                        if(location!=null) {
                            requireActivity().runOnUiThread(object :Runnable{
                                override fun run() {
                                    val marker = googleMap.addMarker(
                                        MarkerOptions().position(
                                            LatLng(
                                                location.lat!!.toDouble(),
                                                location.lng!!.toDouble()
                                            )
                                        ).title(location.name+", "+it.serviceName)
                                           /* .icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))*/)

                                    marker!!.tag = it
                                    if(it.dateTimeForService!=null && it.dateTimeForService!="")
                                        marker.setIcon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                    else
                                    {
                                        marker.setIcon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    }
                                }

                            })

                        }
                    }
                }
            }


        }



    }
    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(
            context, vectorResId
        )


        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable!!.intrinsicWidth,
            vectorDrawable!!.intrinsicHeight
        )


        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable!!.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )


        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)


        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable!!.draw(canvas)


        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}