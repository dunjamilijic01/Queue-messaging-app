package com.example.qmsapp

import android.app.Activity
import android.util.Log

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.qmsapp.data.Region
import com.example.qmsapp.models.RegionsViewModel

class CategoryListAdapter(private val context: Activity, private val dataSet:ArrayList<Region>,private val regionsViewModel:RegionsViewModel)
    : /*ArrayAdapter<String>(context,R.layout.place_branches_list_view_item,dataSet)*/BaseAdapter() {

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(p0: Int): Any {
        return  dataSet.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.categories_list_view_item, null, true)
        val branchName = view.findViewById<TextView>(R.id.categoryNametextView)
        val imageView = view.findViewById<ImageView>(R.id.categoryIcon)
        val arrowIcon=view.findViewById<ImageView>(R.id.arrowIcon)

            val region: com.example.qmsapp.data.Region = dataSet[position]
            regionsViewModel.getIcon(region.regionId) { icon ->

                icon?.let {
                    try {
                        context.runOnUiThread(object : Runnable {
                            override fun run() {
                                imageView.setImageBitmap(it)
                            }

                        })
                    } catch (ex: Exception) {
                        Log.d("ex", ex.toString())
                    }
                    //
                }

                branchName.text = region.regionName
                arrowIcon.visibility=View.VISIBLE

            }

        return view
    }
}