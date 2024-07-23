package com.example.qmsapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.GridLayout
import android.widget.GridLayout.spec
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.qmsapp.databinding.FragmentReserveBinding
import com.example.qmsapp.models.NotificationViewModel
import com.example.qmsapp.models.ReservationsViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DayViewDecorator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_CALENDAR
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ReserveFragment : Fragment() {

    private var _binding: FragmentReserveBinding? = null
    private val binding get() = _binding!!
    private var datePicker: MaterialDatePicker<Long>? = null
    val availableDates: ArrayList<Date> = ArrayList<Date>()
    private var appointment:String=""
    lateinit var firebaseAuth: FirebaseAuth
    private val reservationViewModel: ReservationsViewModel by activityViewModels()
    private val notificationViewModel:NotificationViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val parentActivity = activity as ReservationActivity
        _binding = FragmentReserveBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth=FirebaseAuth.getInstance()
        val parent = activity as ReservationActivity
        parent.setActionBarTitle("REZERVACIJA",true)

        reservationViewModel.getAvailableDates(parent.serviceId!!) {
            it?.let {

                val availableDateStrings: ArrayList<String> = it
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                availableDateStrings.forEach {
                    val date: Date = formatter.parse(it)
                    availableDates.add(date)
                }

                Log.d("datess", availableDates.toString())

                val constraintsBuilder =
                    CalendarConstraints.Builder()
                        .setValidator(object : CalendarConstraints.DateValidator {
                            override fun describeContents(): Int {
                                return 0
                            }

                            override fun writeToParcel(p0: Parcel, p1: Int) {
                            }

                            override fun isValid(date: Long): Boolean {
                                val d: Date = Date(date)
                                d.hours = 0
                                Log.d("dates", Date(date).toString())
                                var contains: Boolean = false
                                return availableDates.contains(d)
                            }

                        })

                datePicker = MaterialDatePicker.Builder.datePicker()
                    .setInputMode(INPUT_MODE_CALENDAR)
                    .setTitleText("Izaberite datum rezervacije")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraintsBuilder.build())
                    .setDayViewDecorator(object : DayViewDecorator() {
                        override fun describeContents(): Int {
                            return 0
                        }

                        override fun writeToParcel(p0: Parcel, p1: Int) {

                        }

                    })
                    .build()



                datePicker!!.show(activity!!.supportFragmentManager, "picker")
                datePicker!!.addOnPositiveButtonClickListener {
                    val dateString:String = formatter.format( Date(it))
                    binding.reservationDateTextView.text=dateString
                    binding.appointmentsLabelTextView.visibility= View.VISIBLE
                    showAppointments(dateString)

                   /* val appointments = arrayOf("10:30","11:30","11:50","13:50","16:30","20:00")
                    val gridViewAdapter= GridViewAdapter(activity!!,appointments)

                    val gridLayout=binding.gridLayout
                    gridLayout.adapter=gridViewAdapter*/
                }
            }



            binding.chooseDateButton.setOnClickListener {

                val constraintsBuilder =
                    CalendarConstraints.Builder()
                        .setValidator(object : CalendarConstraints.DateValidator {
                            override fun describeContents(): Int {
                                return 0
                            }

                            override fun writeToParcel(p0: Parcel, p1: Int) {
                            }

                            override fun isValid(date: Long): Boolean {
                                val d: Date = Date(date)
                                d.hours = 0
                                Log.d("dates", Date(date).toString())
                                var contains: Boolean = false
                                return availableDates.contains(d)
                            }

                        })

                datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setInputMode(INPUT_MODE_CALENDAR)
                        .setTitleText("Izaberite datum rezervacije")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder.build())
                        .setDayViewDecorator(object : DayViewDecorator() {
                            override fun describeContents(): Int {
                                return 0
                            }

                            override fun writeToParcel(p0: Parcel, p1: Int) {

                            }

                        })
                        .build()


                datePicker!!.show(activity!!.supportFragmentManager, "picker")
                datePicker!!.addOnPositiveButtonClickListener {

                    Toast.makeText(context, "cao", Toast.LENGTH_SHORT).show()
                       val formatter:SimpleDateFormat =  SimpleDateFormat("yyyy-MM-dd");
                       val dateString:String = formatter.format( Date(it))
                       binding.reservationDateTextView.text=dateString
                       binding.appointmentsLabelTextView.visibility= View.VISIBLE

                        showAppointments(dateString)
            }
            }



            binding.reserveButton.setOnClickListener {
                if (binding.reservationDateTextView.text != "" && binding.reservationDateTextView.text != null) {
                    if(appointment!="") {

                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Potvrda termina")
                        builder.setMessage("Vas termin : ${binding.reservationDateTextView.text} u ${appointment} \n\nDa li ste sigutni da zelite da rezervisete odabrani temrin?")
                        builder.setPositiveButton("DA",object :DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val bundle = Bundle()
                                bundle.putString("date", binding.reservationDateTextView.text.toString())
                                bundle.putString("appointment",appointment)
                                bundle.putString("parent","reserve")

                                val dateTime=LocalDateTime.parse("${binding.reservationDateTextView.text} ${appointment}",
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                reservationViewModel.makeAReservation(parent.serviceId!!,firebaseAuth.currentUser!!.email!!,dateTime.toString()){ pair->
                                     if(pair!=null)
                                     {
                                         if(pair.first!=null)
                                         {
                                             notificationViewModel.subscribe(firebaseAuth.currentUser!!.email!!, parentActivity.token!!) {response->
                                                 Log.d("notif",response!!)
                                             }
                                             val navController = findNavController()
                                             bundle.putString("label",pair.first!!.label)
                                             navController.navigate(R.id.action_reserveFragment_to_ETicketFragment, bundle)
                                         }
                                         else
                                         {
                                             activity!!.runOnUiThread(object :Runnable{
                                                 override fun run() {
                                                     val builder = AlertDialog.Builder(context)
                                                     builder.setTitle("Obevstenje")
                                                     builder.setMessage(pair.second!!+" Ako zelite da promenite termin prvo morate da otkazete prethodno zakazani u sekciji Moji tiketi-Rezervisani tiketi")
                                                     builder.setPositiveButton("OK",object :DialogInterface.OnClickListener{
                                                         override fun onClick(
                                                             p0: DialogInterface?,
                                                             p1: Int) {
                                                         }
                                                     })
                                                     builder.show()
                                                 }

                                             })

                                         }
                                    }
                                    else
                                     {

                                     }

                            }

                        }})
                        builder.setNegativeButton("NE",object :DialogInterface.OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                            }

                        })

                        builder.show()


                    }
                    else
                    {
                        Toast.makeText(context, "Morate odabrati termin", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(context, "Morate odabrati datum", Toast.LENGTH_SHORT).show()
                    binding.reservationDateTextView.text= "REQUIRED!"
                }
            }




        }
        return view
    }

    private fun showAppointments(date:String)
    {

        val parent = activity as ReservationActivity

        reservationViewModel.getAvailableAppointments(date,parent.serviceId!!)
        {
           Log.d("test", "appointments" + it.toString())
            it?.let {
                val appointments = it

                val grid=binding.appointmentGridLayout

                val scale = context!!.getResources().getDisplayMetrics().density;

                try {
                    activity!!.runOnUiThread(object : Runnable {
                        override fun run() {
                            grid.setPadding(20)
                            appointments.forEach {
                                val button = Button(context)
                                button.apply {
                                    gravity= Gravity.CENTER
                                    button.text=it
                                    button.setTextColor(resources.getColor(R.color.white))
                                    button.backgroundTintList=resources.getColorStateList(android.R.color.darker_gray)

                                }
                                button.setOnClickListener {
                                    button.backgroundTintList=resources.getColorStateList(R.color.soft_red_500)
                                    appointment=button.text.toString()
                                    for(i in 0 .. grid.childCount-1)
                                    {
                                        val child = grid.getChildAt(i) as Button


                                        if(child!=button)
                                            child.backgroundTintList=resources.getColorStateList(android.R.color.darker_gray)
                                    }
                                }

                                grid.addView(button)

                            }

                        }
                    })
                }
                catch (ex:Exception)
                {
                    Log.d("ex",ex.toString())
                }

            }

        }
    }
}