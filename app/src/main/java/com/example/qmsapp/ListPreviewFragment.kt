package com.example.qmsapp

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.EdgeEffectCompat.getDistance
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qmsapp.data.Location
import com.example.qmsapp.databinding.FragmentListPreviewBinding
import com.example.qmsapp.models.LocationViewModel

class ListPreviewFragment : Fragment() {

    private var _binding: FragmentListPreviewBinding?=null
    private val binding get()=_binding!!
    private lateinit var  navController: NavController
    private val locationViewModel: LocationViewModel by activityViewModels()
    private var locations:ArrayList<Location> = ArrayList<Location>()
    private lateinit var locationManager:LocationManager
    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentListPreviewBinding.inflate(inflater, container, false)
        val view=binding.root
        Log.d("oncreateview", "uso")

        val fragment = parentFragment as NavHostFragment
        val parent=fragment.parentFragment as CategoryFragment
        val bundle:Bundle?= arguments
        if(parent.region!=null) {
            locationViewModel.getAllLocations(/*bundle.getInt("regionId")*/parent.region!!.regionId) { locs ->
                Log.d("getAllLocations", locs.toString())
                if (locs != null) {
                    locations = ArrayList<Location>()
                    locations = locs
                    Log.d("getAllLocations",locations.toString())
                } else {
                    locations = ArrayList<Location>()
                    Log.d("loc", "nema")
                }

                try {
                    activity!!.runOnUiThread(object : Runnable {
                        override fun run() {
                            Log.d("locationsbeforeadapter", locations.toString())
                            val customAdapter =
                                RecyclerViewAdapter(
                                    context!!,
                                    locations, /*activity!!.supportFragmentManager*/
                                    activity!!.findNavController(R.id.main_fragment_container),
                                    locationViewModel
                                )
                            binding.listPreviewRecyclerView.addItemDecoration(
                                DividerItemDecoration(
                                    context,
                                    LinearLayoutManager.HORIZONTAL
                                )
                            )
                            //binding.listPreviewRecyclerView.addItemDecoration(null)
                            binding.listPreviewRecyclerView.adapter = customAdapter
                            if (locations.size > 0) {
                                binding.listPreviewRecyclerView.visibility = View.VISIBLE
                                binding.noLocationsTextView.visibility = View.GONE
                            } else {
                                binding.listPreviewRecyclerView.visibility = View.GONE
                                binding.noLocationsTextView.visibility = View.VISIBLE
                            }
                            binding.listPreviewRecyclerView.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        }

                    })
                } catch (ex: Exception) {
                    Log.d("ex", ex.toString())
                }


            }
        }

        locationManager= LocationManager(context!!)

        /*locationViewModel._locations.observe(viewLifecycleOwner, Observer {
            locationViewModel._locations.value?.let {
                locations = it
                val customAdapter =
                    RecyclerViewAdapter(context!!, locations, /*activity!!.supportFragmentManager*/activity!!.findNavController(R.id.main_fragment_container),locationViewModel)
                binding.listPreviewRecyclerView.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        LinearLayoutManager.HORIZONTAL
                    )
                )
                binding.listPreviewRecyclerView.adapter = customAdapter
                if(locations.size>0)
                {
                    binding.listPreviewRecyclerView.visibility=View.VISIBLE
                    binding.noLocationsTextView.visibility=View.GONE
                }
                else{
                    binding.listPreviewRecyclerView.visibility=View.GONE
                    binding.noLocationsTextView.visibility=View.VISIBLE
                }

            }

            binding.listPreviewRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        })*/

        locationViewModel._filterRadius.observe(viewLifecycleOwner, Observer {
            progressDialog=ProgressDialog(context)
            progressDialog.setTitle("Please wait")
            progressDialog.setMessage("Please wait..")

            progressDialog.show()
            showFilteredLocations(it)
        })

        return view
    }

   fun showFilteredLocations(distance:Float){
      var filteredLocations:ArrayList<Location> = ArrayList<Location>()

       locationManager.findLocation { loc->
           if(loc!=null) {
               locations.forEach {
                   if (it.lat != null && it.lng != null) {
                       if (getDistance(loc.latitude, loc.longitude, it.lat.toDouble(), it.lng.toDouble()) < distance)
                           filteredLocations.add(it)
                   }
               }
               val customAdapter = RecyclerViewAdapter(context!!, filteredLocations, /*activity!!.supportFragmentManager*/activity!!.findNavController(R.id.main_fragment_container),locationViewModel)
               binding.listPreviewRecyclerView.adapter = customAdapter
               if(filteredLocations.size>0)
               {
                   binding.listPreviewRecyclerView.visibility=View.VISIBLE
                   binding.noLocationsTextView.visibility=View.GONE
               }
               else{
                   binding.listPreviewRecyclerView.visibility=View.GONE
                   binding.noLocationsTextView.visibility=View.VISIBLE
               }
               progressDialog.hide()

           }
       }

  }

    fun getDistance(lat1:Double,lng1:Double,lat2:Double,lng2:Double) :Double{

        val distance: FloatArray = FloatArray(2)
        android.location.Location.distanceBetween(
            lat1,
            lng1,
            lat2,
           lng2,
            distance
        )
        val distance2dig: Double = String.format("%.2f", distance[0] / 1000).toDouble()
        return  distance2dig
    }

}