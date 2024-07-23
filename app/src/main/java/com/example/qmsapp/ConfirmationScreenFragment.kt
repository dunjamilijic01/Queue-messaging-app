package com.example.qmsapp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qmsapp.databinding.FragmentConfirmationScreenBinding
import com.example.qmsapp.databinding.FragmentGetInLineBinding

class ConfirmationScreenFragment : Fragment() {
    private var _binding: FragmentConfirmationScreenBinding?=null
    private val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentConfirmationScreenBinding.inflate(inflater, container, false)
        val view=binding.root
        binding.confirmationScreenTextView.text=arguments!!.getString("text")
        val timer = object:CountDownTimer(3000,5000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                activity!!.finish()
            }

        }
        timer.start()

        return view
    }

}