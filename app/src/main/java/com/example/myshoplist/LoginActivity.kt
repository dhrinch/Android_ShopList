package com.example.myshoplist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        button.setOnLongClickListener{
            auth.createUserWithEmailAndPassword(editText_Email.text.toString(), editText_Password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(this, "Not registered", Toast.LENGTH_SHORT).show()
                    }
                }
            true
        }

        button.setOnClickListener{
            auth.signInWithEmailAndPassword(editText_Email.text.toString(), editText_Password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                    } else{
                        Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show()
                    }
                }
            true
        }
    }
}