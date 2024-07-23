package com.example.qmsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.qmsapp.databinding.FragmentChangeNameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ChangeNameFragment : Fragment() {

    private var _binding:FragmentChangeNameBinding?=null
    private val binding get()=_binding!!
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth=FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentChangeNameBinding.inflate(inflater,container,false)
        val view=binding.root

        binding.buttonSaveName.setOnClickListener {
            if(binding.nameEditText.text.isNotEmpty())
            {
                val userProfileChangeRequest=UserProfileChangeRequest.Builder().setDisplayName(binding.nameEditText.text.toString()).build()
                firebaseAuth.currentUser!!.updateProfile(userProfileChangeRequest).addOnSuccessListener {
                    activity!!.findNavController(R.id.main_fragment_container)
                        .navigate(R.id.action_changeNameFragment_to_accountFragment)
                }
            }
        }
        return view
    }

}