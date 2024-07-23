package com.example.qmsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.qmsapp.databinding.ActivityLogInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LogInActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityLogInBinding
    private  lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth=FirebaseAuth.getInstance()

        binding=ActivityLogInBinding.inflate(layoutInflater)
        val view=binding.root

        binding.usernameEditText.doOnTextChanged { text, start, before, count ->
            if(text!=null && text!="")
                binding.usernameLayoutEditText.error=null
        }

        binding.passwordEditText.doOnTextChanged { text, start, before, count ->
            if(text!=null && text!="")
                binding.passwordLayoutTextField.error=null
        }

        binding.buttonLogIn.setOnClickListener {
            val username:String=binding.usernameEditText.text.toString()
            val password:String=binding.passwordEditText.text.toString()
            if(username=="")
            {
                binding.usernameLayoutEditText.error="Required!"
            }
            else
            {
                if(password=="")
                {
                    binding.passwordLayoutTextField.error="Required"
                }
                else
                {
                    firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(this) {task->
                        if(task.isSuccessful)
                        {
                            val intent:Intent=Intent(applicationContext,MainActivity::class.java)
                            startActivity(intent)
                            binding.passwordEditText.setText("")
                            binding.usernameEditText.setText("")
                        }
                        else
                        {
                            Toast.makeText(applicationContext, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.goToRegisterTextView.setOnClickListener {
            val intent:Intent=Intent(applicationContext,RegisterActivity::class.java)
            startActivity(intent)
            binding.passwordEditText.setText("")
            binding.usernameEditText.setText("")
        }

        setContentView(view)
    }
}