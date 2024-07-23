package com.example.qmsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.qmsapp.R.color
import com.example.qmsapp.R.color.*
import com.example.qmsapp.data.Region
import com.example.qmsapp.databinding.FragmentCategoryBinding
import com.example.qmsapp.databinding.FragmentHomeBinding
import com.example.qmsapp.models.LocationViewModel
import com.example.qmsapp.models.RegionsViewModel
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import org.apache.http.HttpHeaders.IF
import java.math.RoundingMode
import java.text.DecimalFormat


class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding?=null
    private val binding get()=_binding!!
    private lateinit var  navController: NavController
    private val locationViewModel:LocationViewModel by activityViewModels()
    public var region:Region?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Toast.makeText(context, "Inicijalizacija", Toast.LENGTH_SHORT).show()
        _binding= FragmentCategoryBinding.inflate(inflater, container, false)
        val view=binding.root

        val bundle=this.arguments
        region=bundle?.getParcelable("category")!!
        /*locationViewModel.getAllLocations(region!!.regionId)
        {

        }*/

        val parentActivity =activity as MainActivity
        parentActivity.setActionBarTitle(region!!.regionName.toString(),true)


        binding.radiusSlider.addOnSliderTouchListener( object : OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                locationViewModel._filterRadius.value=slider.value
            }

        })

         val navHostFragment: NavHostFragment =
           childFragmentManager?.findFragmentById(R.id.preview_fragment_container) as NavHostFragment
       navController=navHostFragment.navController


        binding.buttonListPreview.isEnabled=false
        binding.buttonMapPreview.isEnabled=true
        binding.buttonListPreview.setBackgroundColor(resources.getColor(R.color.soft_red_500))
        binding.buttonListPreview.setTextColor(resources.getColor(R.color.white))
        //navController.navigate(R.id.listPreviewFragment)


         binding.buttonListPreview.setOnClickListener {

             binding.buttonListPreview.isEnabled=false
             binding.buttonMapPreview.isEnabled=true
             binding.buttonListPreview.setBackgroundColor(resources.getColor(R.color.soft_red_500))
             binding.buttonListPreview.setTextColor(resources.getColor(R.color.white))
             binding.buttonMapPreview.setBackgroundColor(resources.getColor(R.color.white))
             binding.buttonMapPreview.setTextColor(resources.getColor(R.color.soft_red_500))

                 val bundle = Bundle()
                 bundle.putInt("regionId", region!!.regionId)
                 navController.navigate(
                     R.id.action_mapPreviewFragment_to_listPreviewFragment,
                     bundle
                 )


         }
         binding.buttonMapPreview.setOnClickListener {

             binding.buttonListPreview.isEnabled=true
             binding.buttonMapPreview.isEnabled=false

             binding.buttonListPreview.setBackgroundColor(resources.getColor(R.color.white))
             binding.buttonListPreview.setTextColor(resources.getColor(R.color.soft_red_500))
             binding.buttonMapPreview.setBackgroundColor(resources.getColor(R.color.soft_red_500))
             binding.buttonMapPreview.setTextColor(resources.getColor(R.color.white))

             val bundle = Bundle()
             bundle.putInt("regionId",region!!.regionId)
             navController.navigate(R.id.action_listPreviewFragment_to_mapPreviewFragment,bundle)
             /*binding.buttonListPreview.isSelected=false
             binding.buttonMapPreview.isSelected=true*/
         }

        binding.distanceValueTextView.text="${binding.radiusSlider.value} km "
        binding.radiusSlider.addOnChangeListener { slider, value, fromUser ->
            binding.distanceValueTextView.text="${
                DecimalFormat("#.##")
                .apply { roundingMode = RoundingMode.FLOOR }
                .format(value)} km"
        }
        return view
    }



}