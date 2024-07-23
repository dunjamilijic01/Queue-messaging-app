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
import com.example.qmsapp.databinding.FragmentAccountBinding
import com.example.qmsapp.databinding.FragmentAllTicketsBinding
import com.example.qmsapp.models.LocationViewModel
import com.example.qmsapp.models.TicketViewModel
import com.google.android.play.integrity.internal.i
import com.google.firebase.auth.FirebaseAuth

class AllTicketsFragment : Fragment() {
    private var _binding: FragmentAllTicketsBinding?=null
    private val binding get()=_binding!!
    lateinit var firebaseAuth: FirebaseAuth
    private val ticketViewModel: TicketViewModel by activityViewModels()
    private val locationViewModel:LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth=FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAllTicketsBinding.inflate(inflater, container, false)


        val parent= activity as MainActivity
        //parent.actionBar!!.setDisplayHomeAsUpEnabled(true)
        //parent.setTitle("Svi tiketi")
        parent.setActionBarTitle("Svi tiketi",true)
        val dataSet= arrayOf("Tiket 1","Tiket 2","Tiket 3","Tiket 4")

        ticketViewModel.getAllTickets(firebaseAuth.currentUser!!.email!!){ tickets->

            if(tickets!=null)
            {
                if(isAdded) {
                    requireActivity().runOnUiThread(object :Runnable{
                    override fun run() {
                        binding.allETicketsListView.adapter =
                            AllTicketsListAdapter(requireActivity(), tickets,locationViewModel)
                    }

                })
                }

            }
        }


        /*ticketViewModel.getAllTickets(firebaseAuth.currentUser!!.email!!){ tickets->
            /*if(tickets!=null) {
                try {
                        activity!!.runOnUiThread(object : Runnable {
                            override fun run() {


                                if (tickets.size > 0) {
                                    binding.allETicketsListView.visibility = View.VISIBLE
                                    binding.noTicketsTextView.visibility = View.GONE
                                } else {
                                    binding.allETicketsListView.visibility = View.GONE
                                    binding.noTicketsTextView.visibility = View.VISIBLE
                                }

                                binding.allETicketsListView.adapter =
                                    FilteredMyTicketsListViewAdapter(activity!!, tickets,locationViewModel)

                            }

                        })

                       /* binding.allETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
                            val bundle=Bundle()
                            Log.d("run", "uso")
                            bundle.putString("serviceName",tickets[i].serviceName)
                            bundle.putString("label",tickets[i].label)
                            bundle.putString("appointment",tickets[i].dateTimeForService)
                            bundle.putString("info",tickets[i].cardNo)
                            bundle.putInt("id",tickets[i].tid)

                            /*locationViewModel.getLocationForService(tickets[i].serviceId!!){location ->

                                if(location!=null)
                                {
                                    bundle.putString("placeName",location.name)
                                    if(tickets[i].dateTimeForService!=null)
                                    {*/
                                        activity!!.findNavController(R.id.main_fragment_container).navigate(R.id.action_allTicketsFragment_to_myTicketPreviewFragment,bundle)
                                    /*}
                                    else
                                    {
                                        ticketViewModel.getClientsInFrontOfMeForService(tickets[i].serviceId!!,tickets[i].serviceName!!){ numberOfClients->
                                            if(numberOfClients!=null)
                                            {
                                                bundle.putInt("numberOfClients",numberOfClients)
                                                activity!!.findNavController(R.id.main_fragment_container).navigate(R.id.action_allTicketsFragment_to_myTicketPreviewFragment,bundle)
                                            }
                                        }
                                    }

                                }*/
                                else
                                {
                                    Log.d("greska","location je null")
                                }


                            }*/



                } catch (ex: Exception) {
                    Log.d("ex", ex.toString())
                }
            }*/

        }



        ticketViewModel._tickets.observe(viewLifecycleOwner, Observer {

            if(it!=null) {
                Log.d("tes", it.toString())

                binding.allETicketsListView.adapter =
                    FilteredMyTicketsListViewAdapter(activity!!, it, locationViewModel)

                /*if (it.size > 0) {
                    binding.allETicketsListView.visibility = View.VISIBLE
                    binding.noTicketsTextView.visibility = View.GONE
                } else {
                    binding.allETicketsListView.visibility = View.GONE
                    binding.noTicketsTextView.visibility = View.VISIBLE
                }*/


            }

        })

        binding.allETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
            val bundle = Bundle()
            Log.d("run", "uso")
            bundle.putString("serviceName", ticketViewModel._tickets.value!!.get(i).serviceName)
            bundle.putString("label",  ticketViewModel._tickets.value!!.get(i).label)
            bundle.putString("appointment", ticketViewModel._tickets.value!!.get(i).dateTimeForService)
            bundle.putString("info",  ticketViewModel._tickets.value!!.get(i).cardNo)
            bundle.putInt("id",  ticketViewModel._tickets.value!!.get(i).tid)
            bundle.putString("dateTime", ticketViewModel._tickets.value!!.get(i).dateTime)


            activity!!.findNavController(R.id.main_fragment_container).navigate(
                R.id.action_allTicketsFragment_to_myTicketPreviewFragment,
                bundle
            )


            /*locationViewModel.getLocationForService(it[i].serviceId!!) { location ->
                if (location != null) {
                    bundle.putString("placeName", location.name)
                    activity!!.findNavController(R.id.main_fragment_container).navigate(
                        R.id.action_allTicketsFragment_to_myTicketPreviewFragment,
                        bundle
                    )

                }


            }*/


        }*/




        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)


        /*ticketViewModel._tickets.observe(viewLifecycleOwner, Observer {

            if(it!=null) {
                Log.d("tes", it.toString())

                binding.allETicketsListView.adapter =
                    AllTicketsListAdapter(requireActivity(), it,locationViewModel)
            }
        })*/

        binding.allETicketsListView.setOnItemClickListener { adapterView, view, i, l ->
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
                R.id.action_allTicketsFragment_to_myTicketPreviewFragment,
                bundle
            )
        }


    }
}