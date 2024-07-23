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
import com.example.qmsapp.databinding.FragmentQueuedTicketsBinding
import com.example.qmsapp.models.LocationViewModel
import com.example.qmsapp.models.TicketViewModel
import com.google.firebase.auth.FirebaseAuth

class QueuedTicketsFragment : Fragment() {

    private var _binding:FragmentQueuedTicketsBinding?=null
    private val binding get()=_binding!!
    lateinit var firebaseAuth: FirebaseAuth
    private val ticketViewModel: TicketViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth= FirebaseAuth.getInstance()
        _binding=FragmentQueuedTicketsBinding.inflate(inflater,container,false)

        val parent= activity as MainActivity
        //parent.setTitle("Tiketi u redu cekanja")
        parent.setActionBarTitle("Tiketi u redu cekanja",true)

        val dataSet= arrayOf("Tiket 1","Tiket 2","Tiket 3","Tiket 4")
        ticketViewModel.getQueuedTickets(firebaseAuth.currentUser!!.email!!){ tickets->
            if(tickets!=null)
            {
                if(isAdded) {
                    requireActivity().runOnUiThread(object :Runnable{
                        override fun run() {
                            binding.queuedETicketsListView.adapter =
                                QueuedTicketsListAdapter(requireActivity(), tickets,locationViewModel)
                        }

                    })
                }

            }
           /* if(tickets!=null) {
                try {
                        parent.runOnUiThread(object : Runnable {
                            override fun run() {

                                if (tickets.size > 0) {
                                    binding.queuedETicketsListView.visibility = View.VISIBLE
                                    binding.noQueuedTicketsTextView.visibility = View.GONE
                                } else {
                                    binding.queuedETicketsListView.visibility = View.GONE
                                    binding.noQueuedTicketsTextView.visibility = View.VISIBLE
                                }

                                binding.queuedETicketsListView.adapter =
                                    FilteredMyTicketsListViewAdapter(activity!!, tickets,locationViewModel)

                            }

                        })


                   /* binding.queuedETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
                        val bundle = Bundle()
                        bundle.putString("serviceName", tickets[i].serviceName)
                        bundle.putString("label", tickets[i].label)
                        bundle.putString("appointment", tickets[i].dateTimeForService)
                        bundle.putString("info", tickets[i].cardNo)
                        bundle.putInt("id", tickets[i].tid)

                        /* locationViewModel.getLocationForService(tickets[i].serviceId!!){location ->
                            if(location!=null)
                            {
                                bundle.putString("placeName",location.name)
                                ticketViewModel.getClientsInFrontOfMeForService(tickets[i].serviceId!!,tickets[i].serviceName!!){numberOfClients->
                                    if(numberOfClients!=null)
                                    {
                                        bundle.putInt("numberOfClients",numberOfClients)
                                        */activity!!.findNavController(R.id.main_fragment_container)
                        .navigate(
                            R.id.action_queuedTicketsFragment_to_myTicketPreviewFragment,
                            bundle
                        )
                        //}
                        //}

                        //}


                        // }
                    }*/

                    }
                 catch (ex: Exception) {
                    Log.d("ex", ex.toString())
                }
            }*/
        }


        /*ticketViewModel._tickets.observe(viewLifecycleOwner, Observer {

            Log.d("test",it.toString())
            binding.queuedETicketsListView.adapter =
                QueuedTicketsListAdapter(requireActivity(), it,locationViewModel)

            if (it.size > 0) {
                binding.queuedETicketsListView.visibility = View.VISIBLE
                binding.noQueuedTicketsTextView.visibility = View.GONE
            } else {
                binding.queuedETicketsListView.visibility = View.GONE
                binding.noQueuedTicketsTextView.visibility = View.VISIBLE
            }*/

            binding.queuedETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
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
                    R.id.action_queuedTicketsFragment_to_myTicketPreviewFragment,
                    bundle
                )


            }

       // })

        val view =binding.root
        return view
    }
}