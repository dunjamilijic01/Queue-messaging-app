package com.example.qmsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.qmsapp.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment() {

    private var _binding:FragmentChangePasswordBinding?=null
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

        _binding= FragmentChangePasswordBinding.inflate(inflater,container,false)
        val view=binding.root

        binding.buttonChangePassword.setOnClickListener {
            if(binding.oldPassEditText.text.isNotEmpty())
            {
                if(binding.newPassEditText.text.isNotEmpty())
                {
                    if(binding.confirmNewPassEditText.text.isNotEmpty())
                    {
                        if(binding.newPassEditText.text.toString() == binding.confirmNewPassEditText.text.toString())
                        {
                            firebaseAuth.signInWithEmailAndPassword(firebaseAuth.currentUser!!.email!!,binding.oldPassEditText.text.toString()).addOnSuccessListener {
                                firebaseAuth.currentUser!!.updatePassword(binding.newPassEditText.text.toString()).addOnSuccessListener {
                                    activity!!.findNavController(R.id.main_fragment_container).navigate(R.id.action_changePasswordFragment_to_accountFragment)
                                }
                                    .addOnFailureListener {
                                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                        Log.d("pass",it.message.toString())
                                    }
                            }

                        }
                        else
                        {
                            Toast.makeText(context, "Lozinke se ne podudaraju!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Morate uneti ponovljenu lozinku!", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(context, "Morate uneti novu lozinku!", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(context, "Morate uneti staru lozinku!", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}