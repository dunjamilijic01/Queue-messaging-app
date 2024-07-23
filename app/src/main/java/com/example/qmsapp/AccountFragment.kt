package com.example.qmsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.qmsapp.databinding.FragmentAccountBinding
import com.example.qmsapp.databinding.FragmentPlaceBinding
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private var _binding: FragmentAccountBinding?=null
    private val binding get()=_binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        firebaseAuth= FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAccountBinding.inflate(inflater, container, false)

        val parent =activity as MainActivity
        parent.setActionBarTitle("Moj profil",false)
        if(firebaseAuth.currentUser!!.displayName!="" && firebaseAuth.currentUser!!.displayName!=null) {
            binding.accountName.text = firebaseAuth.currentUser!!.displayName
            binding.accountName.setTextColor( resources.getColor(com.google.android.material.R.color.m3_default_color_primary_text))
        }
        else
        {
            binding.accountName.setTextColor( resources.getColor(R.color.soft_red_700))
            binding.accountName.text="Unesite ime i prezime"

        }
        binding.accountPasswordChange.setOnClickListener {
            activity!!.findNavController(R.id.main_fragment_container).navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }
        binding.accountEmail.text=firebaseAuth.currentUser!!.email
        binding.accountLogOut.setOnClickListener {
            firebaseAuth.signOut()
            activity!!.finish()
        }
        binding.accountNameChange.setOnClickListener {
            activity!!.findNavController(R.id.main_fragment_container)
                .navigate(R.id.action_accountFragment_to_changeNameFragment)
        }
        val view=binding.root
        return view

    }



}