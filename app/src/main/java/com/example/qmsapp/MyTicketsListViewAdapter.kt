package com.example.qmsapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.qmsapp.data.Region
import com.example.qmsapp.models.RegionsViewModel

class MyTicketsListViewAdapter(private val context: Activity, private val dataSet:Array<String>)
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
        val view = inflater.inflate(R.layout.my_tickets_list_view_item, null, true)
        view.findViewById<TextView>(R.id.myTicketsListViewItemTextView).text=dataSet[p0]


        return view
    }
}