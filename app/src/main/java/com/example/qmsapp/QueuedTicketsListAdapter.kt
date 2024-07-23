package com.example.qmsapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.qmsapp.data.Ticket
import com.example.qmsapp.models.LocationViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class QueuedTicketsListAdapter (private val context: Activity, private val dataSet:ArrayList<Ticket>,public val locationViewModel: LocationViewModel)
    : BaseAdapter() {
    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(p0: Int): Any {
        return  dataSet[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.my_e_ticket, null, true)


        if(dataSet[p0].serviceName!="NEMA TIKETA") {

            view.findViewById<TextView>(R.id.messageTextView).visibility=View.GONE
            view.findViewById<CardView>(R.id.myTicketCardView).visibility=View.VISIBLE
            view.findViewById<TextView>(R.id.myETicketPlaceNameTextView).text =
                dataSet[p0].serviceName
            view.findViewById<TextView>(R.id.myETicketPlaceAddressTextView).text =
                dataSet[p0].locationName
            view.findViewById<Button>(R.id.liveQueueButton).visibility=View.VISIBLE
            view.findViewById<Button>(R.id.liveQueueButton).setOnClickListener {
                context.findNavController(R.id.main_fragment_container).navigate(R.id.action_queuedTicketsFragment_to_liveQueueFragment)
            }

            if (dataSet[p0].dateTimeForService != null && dataSet[p0].dateTimeForService != "") {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val output: String = formatter.format(parser.parse(dataSet[p0].dateTimeForService))
                view.findViewById<TextView>(R.id.myeTicketStatusTextView).text = "Rezervisano"
                view.findViewById<TextView>(R.id.myETicketDateTimeTextView).text = output
            } else {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val output: String = formatter.format(parser.parse(LocalDateTime.now().toString()))
                view.findViewById<TextView>(R.id.myeTicketStatusTextView).text = "U redu cekanja"
                view.findViewById<TextView>(R.id.myETicketDateTimeTextView).text = output
            }

            locationViewModel.getLocationForService(dataSet[p0].serviceId!!){location ->
                if(location!=null)
                {
                    context.runOnUiThread(object :Runnable{
                        override fun run() {
                            view.findViewById<TextView>(R.id.myETicketPlaceAddressTextView).text =
                                location.name+", "+location.city
                        }

                    })
                }
            }

        }
        else{
            view.findViewById<TextView>(R.id.messageTextView).visibility=View.VISIBLE
            view.findViewById<CardView>(R.id.myTicketCardView).visibility=View.GONE
        }


        return view
    }
}