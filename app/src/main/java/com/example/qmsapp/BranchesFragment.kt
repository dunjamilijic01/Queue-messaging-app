package com.example.qmsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qmsapp.databinding.FragmentBranchesBinding



class BranchesFragment : Fragment() {

    private var _binding: FragmentBranchesBinding?=null
    private val binding get()=_binding!!
    //private lateinit var  navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentBranchesBinding.inflate(inflater, container, false)

        val listViewData= arrayOf("januar","februar","mart","april","maj","jun","jul")
        binding.branchesListView.adapter=BranchesListAdapter(activity!!,listViewData)

        binding.branchesListView.setOnItemClickListener { adapterView, view, position, id ->
            val item=adapterView.getItemAtPosition(position)
            Toast.makeText(context!!,item.toString(),Toast.LENGTH_SHORT).show()
            val placeFragment=PlaceFragment()

            activity!!.supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container,placeFragment).addToBackStack(null).commit()
        }


        val view=binding.root

        return view
    }

}