package com.example.qmsapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.example.qmsapp.data.Ticket
import com.example.qmsapp.models.LocationViewModel
import com.google.api.client.util.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class FilteredMyTicketsListViewAdapter(val context:Activity, val dataSet:ArrayList<Ticket>,locationViewModel: LocationViewModel):BaseAdapter() {

    val locViewModel=locationViewModel
    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(p0: Int): Any {
        return dataSet[p0]
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.my_e_ticket, null, true)

        Log.d("size",dataSet.size.toString())
        if(dataSet[p0].serviceName!="NEMA TIKETA") {

            view.findViewById<CardView>(R.id.myTicketCardView).visibility=View.VISIBLE
            view.findViewById<TextView>(R.id.messageTextView).visibility=View.GONE
            if (dataSet[p0].dateTimeForService != null) {
                //view.findViewById<TextView>(R.id.myETicketDateTimeTextView).text =
                /*val inputDate=LocalDateTime.parse(dataSet[p0].dateTimeForService,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))*/
                /* val outputDate=inputDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                 view.findViewById<TextView>(R.id.myETicketDateTimeTextView).text =outputDate.toString()*/
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val output: String = formatter.format(parser.parse(dataSet[p0].dateTimeForService))
                view.findViewById<TextView>(R.id.myETicketDateTimeTextView).text = output
                view.findViewById<TextView>(R.id.myeTicketStatusTextView).text = "Rezervisano"

            } else {
                val dateTime = LocalDateTime.now()
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val output: String = formatter.format(parser.parse(dateTime.toString()))
                view.findViewById<TextView>(R.id.myeTicketStatusTextView).text = "U redu cekanja"
                view.findViewById<TextView>(R.id.myETicketDateTimeTextView).text = output
            }


            view.findViewById<TextView>(R.id.myETicketPlaceNameTextView).text =
                dataSet[p0].serviceName
            view.findViewById<TextView>(R.id.myETicketPlaceAddressTextView).text =
                dataSet[p0].locationName
            locViewModel.getLocationForService(dataSet[p0].serviceId!!) { location ->
                if (location != null)
                    view.findViewById<TextView>(R.id.myETicketPlaceAddressTextView).text =
                        location.name + ", " + location.city
                else
                    view.findViewById<TextView>(R.id.myETicketPlaceAddressTextView).text = "Nema"


            }
        }
        else
        {
            /*view.findViewById<TextView>(R.id.myETicketPlaceNameTextView).text =
                "NEMA TIKETA"
            view.findViewById<TextView>(R.id.myETicketPlaceAddressTextView).text =""
            view.findViewById<TextView>(R.id.myeTicketStatusTextView).text = ""
            view.findViewById<TextView>(R.id.myETicketDateTimeTextView).text = ""*/
            view.findViewById<CardView>(R.id.myTicketCardView).visibility=View.GONE
            view.findViewById<TextView>(R.id.messageTextView).visibility=View.VISIBLE


        }


        return  view

    }
}