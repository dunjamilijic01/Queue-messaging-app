package com.example.qmsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.qmsapp.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.buttonRegister.setOnClickListener {
            val username:String=binding.usernameEditText.text.toString()
            val password:String=binding.passwordEditText.text.toString()
            val confirmPassword:String=binding.confirmPasswordEditText.text.toString()
            if (username == "") {
                binding.usernameLayoutEditText.error="Required"
            }
            else {
                if (password == "") {
                    binding.passwordLayoutEditText.error="Required"
                }
                else {
                    if (confirmPassword == "") {
                        binding.confirmPasswordLayoutEditText.error="Required"
                    }
                    else {
                        if (password == confirmPassword) {
                            firebaseAuth.createUserWithEmailAndPassword(username, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        val intent: Intent =
                                            Intent(applicationContext, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            task.exception?.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                        }
                        else {
                            Toast.makeText(
                                applicationContext,
                                "Passwords not matching",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.usernameEditText.doOnTextChanged { text, start, before, count ->
            if(text!=null && text!="")
                binding.usernameLayoutEditText.error=null
        }

        binding.passwordEditText.doOnTextChanged { text, start, before, count ->
            if(text!=null && text!="")
                binding.passwordLayoutEditText.error=null
        }

        binding.confirmPasswordEditText.doOnTextChanged { text, start, before, count ->
            if(text!=null && text!="")
                binding.confirmPasswordLayoutEditText.error=null
        }

        binding.goToLogInTextView.setOnClickListener {
            finish()
        }

    }
}