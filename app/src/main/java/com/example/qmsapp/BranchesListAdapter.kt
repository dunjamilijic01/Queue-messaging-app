package com.example.qmsapp

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class BranchesListAdapter(private val context: Activity,private val dataSet:Array<String>)
    :ArrayAdapter<String>(context,R.layout.place_branches_list_view_item,dataSet) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater=context.layoutInflater
        val view=inflater.inflate(R.layout.place_branches_list_view_item,null,true)

        val branchName=view.findViewById<TextView>(R.id.branchNameTextView)
        branchName.text= dataSet[position]
        return view
    }

}