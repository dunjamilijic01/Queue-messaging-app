package com.example.qmsapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.qmsapp.data.Location
import com.example.qmsapp.databinding.FragmentMapPreviewBinding
import com.example.qmsapp.models.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog


class MapPreviewFragment : Fragment() ,OnMapReadyCallback{

    private var _binding: FragmentMapPreviewBinding?=null
    private val binding get()=_binding!!
    private lateinit var googleMap:GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var locationManager:LocationManager
    private lateinit var bundle: Bundle

    //private lateinit var navController:NavController

    private val locationViewModel: LocationViewModel by activityViewModels()
    private var locations:ArrayList<Location> = ArrayList<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Toast.makeText(context, "on create view", Toast.LENGTH_SHORT).show()
        if(arguments!=null)
            bundle = arguments as Bundle
        else
            bundle= Bundle()

        locationManager= LocationManager(context!!)
        _binding= FragmentMapPreviewBinding.inflate(inflater, container, false)

        locationViewModel._locations.observe(viewLifecycleOwner, Observer {
            locationViewModel._locations.value?.let {
                locations = it
            }
        })
        locationViewModel._filterRadius.observe(viewLifecycleOwner, Observer {
            showFilteredLocations(it)
        })

        mapFragment=childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
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
                val cameraPosition=CameraPosition(loc,16f,0f,0f)
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

        addLocationsOnMap()

        googleMap.setOnMarkerClickListener {
                val loc: Location = it.tag as Location
                //Toast.makeText(context, loc.toString(), Toast.LENGTH_SHORT).show()

                val bundle = Bundle()
                bundle.putInt("id", loc.locId)
                bundle.putString("address", loc.address)
                bundle.putString("opensAt", loc.opensAt.toString())
                bundle.putString("closesAt", loc.closesAt.toString())
                bundle.putString("name", loc.name)
                bundle.putFloat("lat",loc.lat!!.toFloat())
                bundle.putFloat("lng",loc.lng!!.toFloat())

                val placeFragment = PlaceFragment()
                placeFragment.arguments = bundle

            val bottomSheetDialog = BottomSheetDialog(context!!, com.google.android.material.R.style.Base_Theme_Material3_Light_BottomSheetDialog)
            val bottomSheetDIalogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout,
            view!!.findViewById(R.id.standard_bottom_sheet))

            bottomSheetDIalogView.findViewById<TextView>(R.id.locationNameSheetTextView).text=loc.name
            bottomSheetDIalogView.findViewById<TextView>(R.id.locationAddressSheetTextView).text=loc.address
            bottomSheetDIalogView.findViewById<Button>(R.id.bottomSheetShowDetailsButton).setOnClickListener {
                /*activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, placeFragment).addToBackStack(null)
                    .commit()*/
                    bottomSheetDialog.hide()
                activity!!.findNavController(R.id.main_fragment_container).navigate(R.id.action_categoryFragment_to_placeFragment,bundle)
            }

            bottomSheetDIalogView.findViewById<Button>(R.id.bottomSheetDirectionsButton).setOnClickListener {
                locationManager.findLocation { currLocation->
                    if(currLocation!=null)
                    {
                        val uri: String =
                            "http://maps.google.com/maps?f=d&hl=en&saddr=${currLocation.latitude},${currLocation.longitude}&daddr=${loc.lat},${loc.lng}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(Intent.createChooser(intent, "Select an application"))
                    }
                }
            }
            bottomSheetDialog.setContentView(bottomSheetDIalogView)
            bottomSheetDialog.show()

            true
        }

    }

    fun addLocationsOnMap(){

        //Toast.makeText(context, "add locations on map", Toast.LENGTH_SHORT).show()
        googleMap.clear()
        if(locations!=null)
        {
            //Toast.makeText(, "", Toast.LENGTH_SHORT).show()
            locations.forEach {
                if(it.lat!=null && it.lng!=null)
                {
                    val marker=  googleMap.addMarker(MarkerOptions().position(LatLng(it.lat.toDouble(),it.lng.toDouble())).title(it.name))
                    marker!!.tag=it
                }
            }
        }
        else{
            //Toast.makeText(context, "locations null", Toast.LENGTH_SHORT).show()
        }
    }

    fun showFilteredLocations(distance:Float){
        locationManager.findLocation { loc->
            if(loc!=null) {
                locations = locationViewModel.getFilteredLocations(distance,loc.latitude,loc.longitude)
                addLocationsOnMap()
            }
        }

    }

}
