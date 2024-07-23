package com.example.qmsapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AppointmentListViewAdapter(val context:Activity,val appointments:ArrayList<String>):
    RecyclerView.Adapter<AppointmentListViewAdapter.ViewHolder>()
{

    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val buttonAppointment: Button
        val context: Context
        init {
            buttonAppointment=view.findViewById(R.id.appointmentButton)
            context=view.context
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentListViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_element_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    override fun onBindViewHolder(holder: AppointmentListViewAdapter.ViewHolder, position: Int) {
        holder.buttonAppointment.text=appointments[position]
        holder.buttonAppointment.setOnClickListener {
            //Toast.makeText(context, Toast.LENGTH_SHORT).show()

            //it.setBackgroundColor(context.resources.getColor(R.color.soft_red_300))
        }

    }

}