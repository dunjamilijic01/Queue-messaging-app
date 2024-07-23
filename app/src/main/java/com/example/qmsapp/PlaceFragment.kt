package com.example.qmsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qmsapp.data.LocationServices
import com.example.qmsapp.databinding.FragmentBranchesBinding
import com.example.qmsapp.databinding.FragmentPlaceBinding
import com.example.qmsapp.models.LocationServicesViewModel
import com.example.qmsapp.models.LocationViewModel
import com.google.android.play.integrity.internal.i
import com.google.android.play.integrity.internal.s

class PlaceFragment : Fragment() {

    private var _binding: FragmentPlaceBinding?=null
    private val binding get()=_binding!!
    private val locationServicesViewModel:LocationServicesViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()
    private lateinit var services:ArrayList<LocationServices>
    lateinit var locationManager:LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var locationId:Int
        var address:String=""
        var opensAt: String
        var closesAt:String
        var name:String=""
        var lat:Float?=null
        var lng:Float?=null
        locationManager= LocationManager(context!!)
        arguments?.let {
            locationId = it.getInt("id")
            address = it.getString("address", "")
            opensAt = it.getString("opensAt", "")
            closesAt = it.getString("closesAt", "")
            name = it.getString("name", "")
            if (it.getFloat("lat") != null)
                lat = it.getFloat("lat")
            if (it.getFloat("lng") != null)
                lng = it.getFloat("lng")

            _binding = FragmentPlaceBinding.inflate(inflater, container, false)
            locationServicesViewModel.getAllServices(locationId)
            { s ->
                if (s != null)
                    services = s
                else
                    services = ArrayList<LocationServices>()

                try {
                    activity!!.runOnUiThread(object : Runnable {
                        override fun run() {
                            if (services.size > 0) {
                                binding.servicesListView.visibility = View.VISIBLE
                                binding.noServicesTextView.visibility = View.GONE
                            } else {
                                binding.servicesListView.visibility = View.GONE
                                binding.noServicesTextView.visibility = View.VISIBLE
                            }

                            locationManager.findLocation { loc ->
                                if (loc != null && lat != null && lng != null)
                                    binding.servicesListView.adapter = ServicesListAdapter(
                                        activity!!,
                                        services,
                                        activity!!.supportFragmentManager,
                                        name,
                                        locationId,
                                        address,
                                        locationViewModel.getDistance(
                                            loc.latitude.toDouble(),
                                            loc.longitude.toDouble(),
                                            lat!!.toDouble(),
                                            lng!!.toDouble()
                                        ),locationServicesViewModel
                                    )
                            }
                            binding.servicesListView.setOnItemClickListener { adapterView, view, position, id ->
                                //adapterView.findViewById<LinearLayout>(R.id.servicesLinearLayout).visibility=View.VISIBLE

                                binding.servicesListView.get(position)
                                    .findViewById<CardView>(R.id.servicesCardView).visibility =
                                    View.VISIBLE
                                //binding.servicesListView.get(position).findViewById<TextView>(R.id.serviceNameTextView).setTextColor(ContextCompat.getColor(context!!,R.color.soft_red_700))
                                binding.servicesListView.get(position).findViewById<LinearLayout>(R.id.listViewItem).background=resources.getDrawable(R.drawable.services_background)
                                for (i in services.indices) {
                                    if (i != position) {
                                        binding.servicesListView.get(i)
                                            .findViewById<CardView>(R.id.servicesCardView).visibility =
                                            View.GONE
                                        binding.servicesListView.get(i).findViewById<LinearLayout>(R.id.listViewItem).background=null
                                        //binding.servicesListView.get(position).findViewById<TextView>(R.id.serviceNameTextView).setTextColor(resources.getColor(R.color.grey))
                                    }

                                }
                            }
                            binding.locationAddressTextView.text = address
                            binding.locationWorkingHoursTextView.text =
                                "Radno vreme: ${opensAt}-${closesAt}"
                            val parentActivity = activity as MainActivity
                            parentActivity.setActionBarTitle(name, true)
                        }
                    })
                } catch (ex: Exception) {
                    Log.d("ex", ex.toString())
                }


            }


            /* locationServicesViewModel._services.observe(viewLifecycleOwner, Observer {

            locationServicesViewModel._services.value?.let {
                services=it
                //Log.d("limitations",it.toString())
                if(services.size>0)
                {
                    binding.servicesListView.visibility=View.VISIBLE
                    binding.noServicesTextView.visibility=View.GONE
                }
                else{
                    binding.servicesListView.visibility=View.GONE
                    binding.noServicesTextView.visibility=View.VISIBLE
                }

                locationManager.findLocation { loc->
                    if(loc!=null && lat!=null && lng!=null)
                        binding.servicesListView.adapter=ServicesListAdapter(activity!!,services,activity!!.supportFragmentManager,name,address,locationViewModel.getDistance(loc.latitude.toDouble(),loc.longitude.toDouble(),lat!!.toDouble(),lng!!.toDouble()))
                }
                binding.servicesListView.setOnItemClickListener { adapterView, view, position, id ->
                    //adapterView.findViewById<LinearLayout>(R.id.servicesLinearLayout).visibility=View.VISIBLE
                    binding.servicesListView.get(position).findViewById<CardView>(R.id.servicesCardView).visibility=View.VISIBLE
                    for(i in services.indices)
                    {
                        if(i!=position)
                            binding.servicesListView.get(i).findViewById<CardView>(R.id.servicesCardView).visibility=View.GONE

                    }
                }
            }

        })*/
        }

        val view=binding.root
        return view
    }

}