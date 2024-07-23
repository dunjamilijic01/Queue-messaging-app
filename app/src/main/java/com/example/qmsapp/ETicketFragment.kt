package com.example.qmsapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qmsapp.databinding.FragmentETicketBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ETicketFragment : Fragment() {
    private var _binding: FragmentETicketBinding?=null
    private val binding get()=_binding!!
    var pageHeight = 1120
    var pageWidth = 792
    var parentFlag:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentETicketBinding.inflate(inflater, container, false)


        //val parent=activity as ReservationActivity
        try{
            val parent=activity as ReservationActivity
            val bundle=arguments
            parent.setActionBarTitle("REZERVACIJA",false)

            bundle?.let {
                binding.eTicketDateTextView.text="${binding.eTicketDateTextView.text} ${it.getString("date")} u ${it.getString("appointment")}"
                parentFlag=it.getString("parent")!!
                binding.eTicketLabelTextView.text=it.getString("label")
                //it.makeText(context, it.getString("appointment"), Toast.LENGTH_SHORT).show()
            }
            binding.eTicketServiceNameTextView.text=parent.serviceTitle
            binding.eTiketLocationNameTextView.text=parent.locarionName
            binding.nextButton.setOnClickListener {
                val bundle=Bundle()
                bundle.putString("text","Uspesno ste napravili rezervaciju!Vase rezervacije mozete pregledati u odeljku \"Moji tiketi\"")
                findNavController().navigate(R.id.action_ETicketFragment_to_confirmationScreenFragment,bundle)
            }
        }
        catch (e:Exception){
            val parent=activity as GetInLineActivity
            val bundle=arguments
            parent.setActionBarTitle("Stani u red",false)
            bundle?.let {
                binding.eTicketDateTextView.text="${binding.eTicketDateTextView.text} ${it.getString("date")}"
                parentFlag=it.getString("parent")!!
                binding.eTicketLabelTextView.text=it.getString("label")
            }
            binding.eTicketServiceNameTextView.text=parent.serviceTitle
            binding.eTiketLocationNameTextView.text=parent.locarionName
            binding.eTicketQueueSizeTextView.text="Broj klijenata ispred vas: 15"

            binding.nextButton.setOnClickListener {
                val bundle=Bundle()
                bundle.putString("text","Uspesno ste stali u red!Vas E-tiket mozete pregledati u odeljku \"Moji tiketi\"")
                findNavController().navigate(R.id.action_ETicketFragment2_to_confirmationScreenFragment2,bundle)
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
        val view=binding.root
        return view
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
            Toast.makeText(context, "PDF uspesno sacuvan", Toast.LENGTH_SHORT).show()
            if(parentFlag=="reserve") {
                val bundle=Bundle()
                bundle.putString("text","Uspesno ste napravili rezervaciju!Vase rezervacije mozete pregledati u odeljku \"Moji tiketi\"")
                findNavController().navigate(R.id.action_ETicketFragment_to_confirmationScreenFragment,bundle)
            }
            else {
                val bundle=Bundle()
                bundle.putString("text","Uspesno ste stali u red!Vas E-tiket mozete pregledati u odeljku \"Moji tiketi\"")
                findNavController().navigate(R.id.action_ETicketFragment2_to_confirmationScreenFragment2,bundle)
            }

        }
        catch (e:Exception)
        {
            Log.d("pdf",e.message.toString())
            Toast.makeText(context, "Greska prilikom kreiranja PDF fajla", Toast.LENGTH_SHORT)
                .show()
        }
        pdfDocument.close()

    }
    fun saveImageToGallery(bitmap: Bitmap)
    {
        val imageName:String="eTicket_${System.currentTimeMillis()}.jpg"
        var fos:OutputStream?=null
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.Q)
        {
            activity!!.contentResolver?.also { resolver->
                val contentValues=ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME,imageName)
                    put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri?=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                fos=imageUri?.let {
                    resolver.openOutputStream(imageUri)
                }
            }
        }
        else
        {
            val imageDirectory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image= File(imageDirectory,imageName)
            fos=FileOutputStream(image)
        }
        fos.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,it!!)
            Toast.makeText(context, "Slika je uspesno sacuvana", Toast.LENGTH_SHORT).show()
            if(parentFlag=="reserve") {
                val bundle=Bundle()
                bundle.putString("text","Uspesno ste napravili rezervaciju!Vase rezervacije mozete pregledati u odeljku \"Moji tiketi\"")
                findNavController().navigate(R.id.action_ETicketFragment_to_confirmationScreenFragment,bundle)
            }
            else {
                val bundle=Bundle()
                bundle.putString("text","Uspesno ste stali u red!Vas E-tiket mozete pregledati u odeljku \"Moji tiketi\"")
                findNavController().navigate(R.id.action_ETicketFragment2_to_confirmationScreenFragment2,bundle)
            }
        }
    }
    fun createBitmapFromView(view:View):Bitmap{
        var bitmap= createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        var canvas:Canvas= Canvas(bitmap)
        var drawable:Drawable=view.background
        if(drawable!=null)
            drawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap

    }

    fun checkPermissions(): Boolean {

        var writeStoragePermission = ContextCompat.checkSelfPermission(
            context!!,
            WRITE_EXTERNAL_STORAGE
        )

        var readStoragePermission = ContextCompat.checkSelfPermission(
            context!!,
            READ_EXTERNAL_STORAGE
        )
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {

        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), 101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.size > 0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(context, "Permission Denied..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}