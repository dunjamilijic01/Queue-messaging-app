package com.example.qmsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.qmsapp.databinding.ActivityGetStartedBinding
import com.google.firebase.auth.FirebaseAuth

class GetStartedActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding : ActivityGetStartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGetStartedBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        firebaseAuth=FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser!=null)
        {
            val intent:Intent=Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val getStarttedButton=binding.getStartedButton.setOnClickListener{
            var intent: Intent = Intent(applicationContext,LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}