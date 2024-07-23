package com.example.qmsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.qmsapp.databinding.FragmentHomeBinding
import com.example.qmsapp.databinding.FragmentMyTicketsBinding

class MyTicketsFragment : Fragment() {
    private var _binding: FragmentMyTicketsBinding?=null
    private val binding get()=_binding!!
    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyTicketsBinding.inflate(inflater, container, false)

        val parent= activity as MainActivity
        parent.setActionBarTitle("Moji tiketi",false)
        //parent.setTitle("Moji tiketi")

        val view = binding.root

        val list = arrayOf("Svi tiketi","Rezervisani tiketi","Tiketi u redu cekanja","Tiketi na mapi")

        binding.myTicketsListView.adapter=MyTicketsListViewAdapter(requireActivity(),list)
        binding.myTicketsListView.setOnItemClickListener { adapterView, view, position, l ->
           try {
               if(position==0)
                   requireActivity().findNavController(R.id.main_fragment_container).navigate(R.id.action_myTicketsFragment_to_allTicketsFragment)
               if(position==1)
                   requireActivity().findNavController(R.id.main_fragment_container).navigate(R.id.action_myTicketsFragment_to_reservedTicketsFragment)
               if(position==2)
                   requireActivity().findNavController(R.id.main_fragment_container).navigate(R.id.action_myTicketsFragment_to_queuedTicketsFragment)
               if(position==3)
                   requireActivity().findNavController(R.id.main_fragment_container).navigate(R.id.action_myTicketsFragment_to_mtTicketsOnMap)
           }
           catch (e:Exception)
           {
               Log.d("nav",e.message.toString())
           }
        }
        return view
    }

}