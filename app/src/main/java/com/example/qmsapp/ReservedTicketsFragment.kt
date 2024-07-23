package com.example.qmsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.qmsapp.data.Ticket
import com.example.qmsapp.databinding.FragmentReservedTicketsBinding
import com.example.qmsapp.models.LocationViewModel
import com.example.qmsapp.models.TicketViewModel
import com.google.firebase.auth.FirebaseAuth

class ReservedTicketsFragment : Fragment() {

    private var _binding:FragmentReservedTicketsBinding?=null
    private val binding get()=_binding!!
    lateinit var firebaseAuth: FirebaseAuth
    private val ticketViewModel: TicketViewModel by activityViewModels()
    private val locationViewModel:LocationViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth= FirebaseAuth.getInstance()
        _binding=FragmentReservedTicketsBinding.inflate(inflater,container,false)

        val parent= activity as MainActivity
        //parent.setTitle("Rezervisani tiketi")
        parent.setActionBarTitle("Rezervisani tiketi",true)
        val dataSet= arrayOf("Tiket 1","Tiket 2","Tiket 3","Tiket 4")

        ticketViewModel.getReservedTickets(firebaseAuth.currentUser!!.email!!){ tickets->
            if(tickets!=null)
            {
                if(isAdded) {
                    requireActivity().runOnUiThread(object :Runnable{
                        override fun run() {
                            binding.reservedlETicketsListView.adapter =
                                ReservedTicketsListAdapter(requireActivity(), tickets,locationViewModel)
                        }

                    })
                }

            }
            /*if(tickets!=null) {
                try {
                        parent.runOnUiThread(object : Runnable {
                            override fun run() {
                                if (tickets.size > 0) {
                                    binding.reservedlETicketsListView.visibility = View.VISIBLE
                                    binding.noReservedTicketsTextView.visibility = View.GONE
                                } else {
                                    binding.reservedlETicketsListView.visibility = View.GONE
                                    binding.noReservedTicketsTextView.visibility = View.VISIBLE
                                }

                                binding.reservedlETicketsListView.adapter =
                                    FilteredMyTicketsListViewAdapter(activity!!, tickets,locationViewModel)

                            }

                        })

                    /*binding.reservedlETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
                        Log.d("t",tickets[i].toString())
                        val bundle=Bundle()
                        bundle.putString("serviceName",tickets[i].serviceName)
                        bundle.putString("label",tickets[i].label)
                        bundle.putString("appointment",tickets[i].dateTimeForService)
                        bundle.putString("info",tickets[i].cardNo)
                        bundle.putInt("id",tickets[i].tid)

                        /*locationViewModel.getLocationForService(tickets[i].serviceId!!){location ->
                            if(location!=null)
                            {
                                bundle.putString("placeName",location.name)*/
                                activity!!.findNavController(R.id.main_fragment_container).navigate(R.id.action_reservedTicketsFragment_to_myTicketPreviewFragment,bundle)
                            //}

                        //}

                    }*/

                    }
                 catch (ex: Exception) {
                    Log.d("ex", ex.toString())
                }
            }*/
        }



        /*ticketViewModel._tickets.observe(viewLifecycleOwner, Observer {

            Log.d("test",it.toString())
            binding.reservedlETicketsListView.adapter =
                ReservedTicketsListAdapter(requireActivity(), it)

            /*if (it.size > 0) {
                binding.reservedlETicketsListView.visibility = View.VISIBLE
                binding.noReservedTicketsTextView.visibility = View.GONE
            } else {
                binding.reservedlETicketsListView.visibility = View.GONE
                binding.noReservedTicketsTextView.visibility = View.VISIBLE
            }*/



        })*/

        binding.reservedlETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
            val bundle = Bundle()
            Log.d("run", "uso")
            bundle.putString("serviceName", ticketViewModel._tickets.value!!.get(i).serviceName)
            bundle.putString("label", ticketViewModel._tickets.value!!.get(i).label)
            bundle.putString(
                "appointment",
                ticketViewModel._tickets.value!!.get(i).dateTimeForService
            )
            bundle.putString("info", ticketViewModel._tickets.value!!.get(i).cardNo)
            bundle.putInt("id", ticketViewModel._tickets.value!!.get(i).tid)
            bundle.putString("dateTime", ticketViewModel._tickets.value!!.get(i).dateTime)
            bundle.putInt("serviceId", ticketViewModel._tickets.value!!.get(i).serviceId!!)


            requireActivity().findNavController(R.id.main_fragment_container).navigate(
                R.id.action_reservedTicketsFragment_to_myTicketPreviewFragment,
                bundle
            )
        }

        /*binding.reservedlETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
            val bundle=Bundle()
            Log.d("run", "uso")
            bundle.putString("serviceName",it[i].serviceName)
            bundle.putString("label",it[i].label)
            bundle.putString("appointment",it[i].dateTimeForService)
            bundle.putString("info",it[i].cardNo)
            bundle.putInt("id",it[i].tid)
            bundle.putString("dateTime",it[i].dateTime)

            /*locationViewModel.getLocationForService(it[i].serviceId!!) {location ->
                if(location!=null)
                {
                    bundle.putString("placeName",location.name)
                    activity!!.findNavController(R.id.main_fragment_container).navigate(R.id.action_reservedTicketsFragment_to_myTicketPreviewFragment,bundle)
                }

            }*/
            activity!!.findNavController(R.id.main_fragment_container).navigate(
                R.id.action_reservedTicketsFragment_to_myTicketPreviewFragment,
                bundle
            )



        }*/
        val view=binding.root
        return view
    }

}