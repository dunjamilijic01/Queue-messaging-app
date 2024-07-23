package com.example.qmsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qmsapp.databinding.FragmentConfirmReservationBinding
import com.example.qmsapp.databinding.FragmentReserveBinding

class ConfirmReservationFragment : Fragment() {
    private var _binding: FragmentConfirmReservationBinding?=null
    private val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parentActivity = activity as ReservationActivity
        _binding= FragmentConfirmReservationBinding.inflate(inflater, container, false)
        val view=binding.root
        binding.reservationConfirmationServiceTextView.text="Usluga: ${parentActivity.serviceTitle}"

        val bundle=arguments
        bundle?.let {
            binding.reservationConfirmationDateTextView.text="Datum: ${it.getString("date")}"
            binding.reservationConfirmationTimeTextView.text="Termin: ${it.getString("appointment")}"
        }

        binding.confirmReservationButton.setOnClickListener {
            findNavController().navigate(R.id.action_reserveFragment_to_ETicketFragment)
        }
        return view
    }
}