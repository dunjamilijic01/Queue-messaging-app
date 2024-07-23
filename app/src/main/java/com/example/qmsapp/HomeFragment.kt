package com.example.qmsapp

import android.Manifest
import android.app.ProgressDialog
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.qmsapp.data.Region
import com.example.qmsapp.databinding.FragmentHomeBinding
import com.example.qmsapp.models.NotificationViewModel
import com.example.qmsapp.models.RegionsViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

import kotlin.concurrent.fixedRateTimer

class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding?=null
    private val binding get()=_binding!!
    private val regionsViewModel:RegionsViewModel by activityViewModels()
    private val notificationViewModel:NotificationViewModel by activityViewModels()
    private var categories:ArrayList<Region> = ArrayList<Region>()
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        firebaseAuth= FirebaseAuth.getInstance()
        /*val navHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController*/

      val parentActivity =activity as MainActivity
        parentActivity.setActionBarTitle("Izaberite kategoriju",false)
        //parentActivity.setTitle("Izaberite kategoriju")


        regionsViewModel.getAllRegions() { regions ->
            /*if (regions != null){
                Log.d("callback","OK")
                categories = regions
            }
            else
            {
                Log.d("callback","null")
            }*/
            if (regions!= null)
                categories = regions
        else
            categories = ArrayList<Region>()


            try {
                activity!!.runOnUiThread(object : Runnable {
                    override fun run() {
                        binding.categoriesListView.adapter = CategoryListAdapter(activity!!, categories,regionsViewModel)

                        if(categories.size>0) {
                            binding.noCategoriesTextView.visibility=View.GONE
                            binding.categoriesListView.visibility=View.VISIBLE
                        }
                        else {
                            binding.noCategoriesTextView.visibility=View.VISIBLE
                            binding.categoriesListView.visibility=View.GONE
                        }
                    }

                })
            } catch (ex: Exception) {
                Log.d("ex", ex.toString())
            }


        }

       /* binding.categoriesListView.adapter =
            CategoryListAdapter(activity!!, categories, regionsViewModel)
        if(categories.size>0) {
            binding.noCategoriesTextView.visibility=View.GONE
            binding.categoriesListView.visibility=View.VISIBLE
        }
        else {
            binding.noCategoriesTextView.visibility=View.VISIBLE
            binding.categoriesListView.visibility=View.GONE
        }*/

        /*regionsViewModel._regions.observe(viewLifecycleOwner, Observer {
            if (regionsViewModel.regions.value != null)
                categories = regionsViewModel.regions.value!!
            else
                categories = ArrayList<Region>()

            binding.categoriesListView.adapter = CategoryListAdapter(activity!!, categories,regionsViewModel)

            if(categories.size>0) {
                binding.noCategoriesTextView.visibility=View.GONE
                binding.categoriesListView.visibility=View.VISIBLE
            }
            else {
                binding.noCategoriesTextView.visibility=View.VISIBLE
                binding.categoriesListView.visibility=View.GONE
            }
        })*/


        binding.categoriesListView.setOnItemClickListener { adapterView, view, position, id ->
            val categoryFragment = CategoryFragment()
            val bundle = Bundle()
            if (categories.size > position)
                bundle.putParcelable("category", categories.get(position))
            else
                bundle.putParcelable("category", null)

            /*categoryFragment.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, categoryFragment).addToBackStack(null)
                .commit()*/
            /*findNavController()*/
            try {
                activity!!.findNavController(R.id.main_fragment_container)
                    .navigate(R.id.action_homeFragment_to_categoryFragment, bundle)
            }
            catch (e:Exception)
            {
                Log.d("nav",e.message.toString())
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("msg", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            notificationViewModel.subscribe(firebaseAuth.currentUser!!.email!!,token){
            }
        })


        return view
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onPause() {
        super.onPause()
    }


}