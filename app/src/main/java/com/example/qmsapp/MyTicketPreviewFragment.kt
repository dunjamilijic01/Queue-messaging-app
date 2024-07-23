package com.example.qmsapp

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.qmsapp.databinding.FragmentMyTicketPreviewBinding
import com.example.qmsapp.models.LocationServicesViewModel
import com.example.qmsapp.models.LocationViewModel
import com.example.qmsapp.models.ReservationsViewModel
import com.example.qmsapp.models.TicketViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class MyTicketPreviewFragment : Fragment() {

    private var _binding:FragmentMyTicketPreviewBinding?=null
    private val binding get()=_binding!!
    private val reservationsViewModel:ReservationsViewModel by activityViewModels()
    private val locationViewModel:LocationViewModel by activityViewModels()
    private val ticketViewModel:TicketViewModel by activityViewModels()
    private val locationServicesViewModel:LocationServicesViewModel by activityViewModels()
    lateinit var firebaseAuth: FirebaseAuth
    private var serviceId:Int?=null
    var dateTime:String?=""
    var pageHeight = 1120
    var pageWidth = 792

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth= FirebaseAuth.getInstance()

        //var placeName:String?=""
        var serviceName:String?=""
        var label:String?=""
        var appointment:String?=""
        var info:String?=""
        var ticketId:Int?=null

        //var serviceId:Int?=null
        var averageWaitingTime:String=""
        arguments?.let {
            //placeName = it.getString("placeName")
            serviceName = it.getString("serviceName")
            label = it.getString("label")
            appointment = it.getString("appointment")
            info = it.getString("info")
            ticketId=it.getInt("id")
            dateTime=it.getString("dateTime")
            serviceId=it.getInt("serviceId")
            //numberOfClients=it.getInt("numberOfClients")

        }

        val parent= activity as MainActivity
        parent.setActionBarTitle("" +
                "",true)

        _binding= FragmentMyTicketPreviewBinding.inflate(inflater,container,false)

        locationViewModel.getLocationForService(serviceId!!){location ->
            if(location!=null)
            {
                requireActivity().runOnUiThread(object :Runnable{
                    override fun run() {
                        binding.eTiketLocationNameTextView.text=location.name
                    }

                })

            }
        }

       /* locationServicesViewModel.getAverageServiceWaitTime(serviceId!!){time->
            if(time!=null)
            {
                averageWaitingTime=time
                /*val startDateTie=LocalDateTime.parse(dateTime!!, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                val endDateTime=LocalDateTime.now()

                if(endDateTime.second>=startDateTie.second)
                {
                    val seconds=endDateTime.second-startDateTie.second
                    if(endDateTime.minute>=startDateTie.minute)
                    {
                        val minutes=endDateTime.minute-startDateTie.minute
                        val hours=endDateTime.hour-startDateTie.hour
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))

                    }
                    else
                    {
                        val minutes=(endDateTime.minute+60)-startDateTie.minute
                        val hours=(endDateTime.hour-1)-startDateTie.hour
                        //binding.waitingTimeTextView.text="${hours}:${minutes}:${seconds}"
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))
                    }
                }
                else
                {
                    val seconds=(endDateTime.second+60)-startDateTie.second
                    if((endDateTime.minute-1)>=startDateTie.minute)
                    {
                        val minutes=(endDateTime.minute-1)-startDateTie.minute
                        val hours=endDateTime.hour-startDateTie.hour
                        //binding.waitingTimeTextView.text="${hours}:${minutes}:${seconds}"
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))
                    }
                    else
                    {
                        val minutes=(endDateTime.minute-1+60)-startDateTie.minute
                        val hours=(endDateTime.hour-1)-startDateTie.hour
                        //binding.waitingTimeTextView.text="${hours}:${minutes}:${seconds}"
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))
                    }
                }*/
            }
        }*/

        checkWaitingtime()

        binding.eTicketLabelTextView.text=label
        binding.eTicketServiceNameTextView.text=serviceName
       // binding.eTiketLocationNameTextView.text=placeName
        if(appointment!="" && appointment!=null)
        {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val output: String = formatter.format(parser.parse(appointment))
            binding.eTicketDateTextView.text="Zakazano za "+output
            binding.queueSizeLinearLayout.visibility=View.INVISIBLE
        }
        else
        {
            binding.waitingTimeLabel.visibility=View.VISIBLE
            //binding.waitingTimeTextView.visibility=View.VISIBLE
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val output: String = formatter.format(parser.parse(LocalDateTime.now().toString()))
            binding.eTicketDateTextView.text="Zakazano za "+output
            binding.cancelButton.visibility=View.GONE
            ticketViewModel.getClientsInFrontOfMeForService(ticketId!!,serviceName!!){clients->
                if(clients!=null)
                {
                    requireActivity().runOnUiThread(object :Runnable{
                        override fun run() {
                            binding.queueSizeLinearLayout.visibility=View.VISIBLE
                            binding.eTicketQueueSizeTextView.text=clients.toString()
                        }

                    })

                }
            }
        }
        binding.cancelButton.setOnClickListener {
            reservationsViewModel.cancelReservation(firebaseAuth.currentUser!!.email!!,ticketId!!){message->

                requireActivity().runOnUiThread(object :Runnable{
                    override fun run() {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Obevstenje")
                        builder.setMessage(message)
                        builder.setPositiveButton("OK",object : DialogInterface.OnClickListener{
                            override fun onClick(
                                p0: DialogInterface?,
                                p1: Int) {

                                activity!!.findNavController(R.id.main_fragment_container).popBackStack()
                                //val parent=activity as MainActivity
                                //parent.onSupportNavigateUp()
                            }
                        })
                        builder.show()
                    }

                })


            }

        }

        binding.buttonDownlaodTicket.setOnClickListener {
            var options= arrayOf("Preuzmi kao sliku","Preuzmi kao PDF")
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Kako zelite da preuzmete tiket?")
            var ticketView=binding.eTicketCardView
            var ticketBitmap=createBitmapFromView(ticketView)
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                if(which==0)
                {
                    if(ticketBitmap!=null)
                    {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
                        saveImageToGallery(ticketBitmap)
                    }
                }
                else {
                    generatePdf(Bitmap.createScaledBitmap(ticketBitmap, pageWidth, pageHeight, false))
                }
            })
            builder.show()
        }

    if(appointment==null || appointment=="") {




        val timer = object : CountDownTimer(30000,10000) {

            override fun onTick(millisUntilFinished: Long) {
                
            }

            override fun onFinish() {
                Log.d("timer","USO")
                ticketViewModel.getClientsInFrontOfMeForService(ticketId!!, serviceName!!) { clients ->
                    if (clients != null) {
                        activity!!.runOnUiThread(object : Runnable {
                            override fun run() {
                                    binding.eTicketQueueSizeTextView.text = clients.toString()
                                    //Toast.makeText(context!!, "timer", Toast.LENGTH_SHORT).show()
                                    checkWaitingtime()
                                    //binding.waitingTimeTextView=
                                }

                        })

                        this.start()
                        Log.d("timer","CLIENT")
                    }
                    else
                    {
                        Log.d("timer","CLIENT je null")
                    }
                }
            }
        }

        timer.start()
    }


      /*  val startDateTie=LocalDateTime.parse(dateTime!!, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        val endDateTime=LocalDateTime.now()

        val milis=Duration.between(startDateTie,endDateTime).toMillis()


        val hms = String.format(
            "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milis),
            TimeUnit.MILLISECONDS.toMinutes(milis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milis
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(milis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milis
                )
            )
        )*/
        //val instant = Instant.from(milis)
        /*binding.countdownTimer.text="${hms}"*/
        binding.info.text=info
        val view=binding.root
        return view
    }

    fun checkWaitingtime(){
        locationServicesViewModel.getAverageServiceWaitTime(serviceId!!){time->
            if(time!=null)
            {
                var leftMins=0
                var leftHours=0

                var averageWaitingTime=time
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val createdDateTime = parser.parse(dateTime)

                val currentTime=LocalTime.now()
                val currentHour=currentTime.hour
                val currentMinute=currentTime.minute
                val waitingHour="${0}${1}".toInt()
                val waitingMinute="${1}${0}".toInt()
                Log.d("waitingTime","${currentHour}:${currentMinute}")
                Log.d("waitingTime","${waitingHour}:${waitingMinute}")

                if(currentMinute-createdDateTime.minutes.toInt()<0)
                {
                    val passedMins=60+currentMinute-createdDateTime.minutes
                    val passedHours=  currentHour-createdDateTime.hours-1
                    Log.d("passed",passedHours.toString()+":"+passedMins.toString())
                    if(waitingMinute-passedMins<0)
                    {
                        leftMins=60+waitingMinute-passedMins
                        leftHours=waitingHour-passedHours-1
                        Log.d("left",leftHours.toString()+":"+leftMins.toString())
                    }
                    else
                    {
                        leftMins= waitingMinute-passedMins
                        leftHours= waitingHour-passedHours
                        Log.d("left",leftHours.toString()+":"+leftMins.toString())
                    }
                }
                else
                {
                    val passedMins=currentMinute-createdDateTime.minutes
                    val passedHours= currentHour-createdDateTime.hours
                    Log.d("passed",passedHours.toString()+":"+passedMins.toString())

                    if(waitingMinute-passedMins<0)
                    {
                        leftMins=60+waitingMinute-passedMins
                        leftHours=waitingHour-passedHours-1
                        Log.d("left",leftHours.toString()+":"+leftMins.toString())
                    }
                    else
                    {
                        leftMins= waitingMinute-passedMins
                        leftHours= waitingHour-passedHours
                        Log.d("left",leftHours.toString()+":"+leftMins.toString())
                    }
                }

                requireActivity().runOnUiThread(object :Runnable{
                    override fun run() {
                        //Toast.makeText(context, createdDateTime.toString(), Toast.LENGTH_SHORT).show()

                        if(leftMins+currentMinute>=60)
                        {
                            val minutes=currentMinute+leftMins-60
                            val hours=currentHour+leftHours+1

                            binding.waitingTimeLabel.text="Procenjeni termin za Vašu uslugu je ${String.format("%02d:%02d", hours, minutes)}"
                        }
                        else
                        {
                            binding.waitingTimeLabel.text="Procenjeni termin za Vašu uslugu je ${String.format("%02d:%02d", currentHour+leftHours, currentMinute+leftMins)}"
                        }

                    }

                })


                /*val startDateTie=LocalDateTime.parse(dateTime!!, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                val endDateTime=LocalDateTime.now()

                if(endDateTime.second>=startDateTie.second)
                {
                    val seconds=endDateTime.second-startDateTie.second
                    if(endDateTime.minute>=startDateTie.minute)
                    {
                        val minutes=endDateTime.minute-startDateTie.minute
                        val hours=endDateTime.hour-startDateTie.hour
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))

                    }
                    else
                    {
                        val minutes=(endDateTime.minute+60)-startDateTie.minute
                        val hours=(endDateTime.hour-1)-startDateTie.hour
                        //binding.waitingTimeTextView.text="${hours}:${minutes}:${seconds}"
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))
                    }
                }
                else
                {
                    val seconds=(endDateTime.second+60)-startDateTie.second
                    if((endDateTime.minute-1)>=startDateTie.minute)
                    {
                        val minutes=(endDateTime.minute-1)-startDateTie.minute
                        val hours=endDateTime.hour-startDateTie.hour
                        //binding.waitingTimeTextView.text="${hours}:${minutes}:${seconds}"
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))
                    }
                    else
                    {
                        val minutes=(endDateTime.minute-1+60)-startDateTie.minute
                        val hours=(endDateTime.hour-1)-startDateTie.hour
                        //binding.waitingTimeTextView.text="${hours}:${minutes}:${seconds}"
                        val avgTime=LocalTime.parse(averageWaitingTime,DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val timePassedString=String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        val timePassed=LocalTime.parse(timePassedString,DateTimeFormatter.ofPattern("HH:mm:ss"))
                    }
                }*/
            }
        }
    }

    fun createBitmapFromView(view:View):Bitmap{
        var bitmap= createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        var canvas: Canvas = Canvas(bitmap)
        var drawable: Drawable =view.background
        if(drawable!=null)
            drawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap

    }


    fun saveImageToGallery(bitmap: Bitmap)
    {
        val imageName:String="eTicket_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream?=null
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.Q)
        {
            requireActivity().contentResolver?.also { resolver->
                val contentValues= ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME,imageName)
                    put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri?=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                fos=imageUri?.let {
                    resolver.openOutputStream(imageUri)
                }
            }
        }
        else
        {
            val imageDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image= File(imageDirectory,imageName)
            fos= FileOutputStream(image)
        }
        fos.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,it!!)
            Toast.makeText(context, "Slika je uspesno sacuvana", Toast.LENGTH_SHORT).show()
        }
    }

    fun generatePdf(bitmap: Bitmap)
    {
        var pdfDocument: PdfDocument = PdfDocument()

        var paint: Paint = Paint()
        var title: Paint = Paint()

        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
        var canvas: Canvas = myPage.canvas
        canvas.drawBitmap(bitmap, 10F, 10F, paint)
        pdfDocument.finishPage(myPage)

        val file: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "E-Ticket${System.currentTimeMillis()}.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(context, "PDf uspesno sacuvan", Toast.LENGTH_SHORT).show()
        }
        catch (e:Exception)
        {
            Log.d("pdf",e.message.toString())
            Toast.makeText(context, "Greska prilikom kreiranja PDF fajla", Toast.LENGTH_SHORT)
                .show()
        }
        pdfDocument.close()

    }
}