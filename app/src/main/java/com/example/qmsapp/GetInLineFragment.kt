package com.example.qmsapp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.qmsapp.databinding.FragmentGetInLineBinding
import com.example.qmsapp.databinding.FragmentPlaceBinding
import com.example.qmsapp.models.LocationServicesViewModel
import com.example.qmsapp.models.LocationViewModel
import com.example.qmsapp.models.ReservationsViewModel
import com.example.qmsapp.models.TicketViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.LocalTime

class GetInLineFragment : Fragment() {
    private var _binding: FragmentGetInLineBinding?=null
    private val binding get()=_binding!!
    private val reservationsViewModel: ReservationsViewModel by activityViewModels()
    private val locationServicesViewModel:LocationServicesViewModel by activityViewModels()
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
        _binding= FragmentGetInLineBinding.inflate(inflater, container, false)
        firebaseAuth=FirebaseAuth.getInstance()
        val view=binding.root
        val parent = activity as GetInLineActivity
        parent.setActionBarTitle("Stani u red",true)

        binding.getInLinePlaceNameTextView.text=parent.locarionName
        binding.getInLinePlaceAddressTextView.text=parent.locationAddress
        binding.getInLineServiceNameTextView.text=parent.serviceTitle

        locationServicesViewModel.getAverageServiceWaitTime(parent.serviceId!!){time->
            if(time!=null) {
                val localTime = LocalTime.parse(time)
                val mainHandler = Handler(context!!.getMainLooper());

                val myRunnable = object : Runnable {
                    override fun run() {
                        binding.getInLineWaitingTimeTExtView.text = "~${localTime.minute} min"
                    }

                }
                mainHandler.post(myRunnable)
            }
            else
            {
                Log.d("count","null je")
            }
        }

        locationViewModel.getClientsForLocation(parent.locationId!!){numberOfClients->
            if(numberOfClients!=null)
            {
                val mainHandler = Handler(context!!.getMainLooper());

                val myRunnable = object : Runnable {
                    override fun run() {
                        binding.getInLineRowCountTExtView.text = numberOfClients.toString()
                    }

                }
                mainHandler.post(myRunnable)
            }
            else
            {
                Log.d("count","null je")
            }

        }



        binding.getinLineButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date", LocalDate.now().toString())
            bundle.putString("appointment","")
            bundle.putString("parent","getInLine")
            reservationsViewModel.getETicket(parent.serviceId!!,firebaseAuth.currentUser!!.email!!){ ticket->
                if(ticket!=null)
                {
                    bundle.putString("label",ticket.label)
                val navController = findNavController()
                navController.navigate(
                    R.id.action_getInLineFragment_to_ETicketFragment2,
                    bundle)
                }
                else
                {
                    //Toast.makeText(context, "Greska prilikom pribavljanja E-tiketa!", Toast.LENGTH_SHORT).show()
                }
            }
                
            
            
        }

        return view
    }

}