package com.example.myshoplist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        sp = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        editor = sp.edit()

        buttonAnon.setOnClickListener {
            auth.signInAnonymously()
            startActivity(Intent(this, MainActivity::class.java))
            true
        }

        buttonLogin.setOnLongClickListener{
            auth.createUserWithEmailAndPassword(editText_Email.text.toString(), editText_Password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else{
                        Toast.makeText(this, "Not registered", Toast.LENGTH_SHORT).show()
                    }
                }
            true
        }

        buttonLogin.setOnClickListener{
            auth.signInWithEmailAndPassword(editText_Email.text.toString(), editText_Password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        editor.putString("UUID", auth.currentUser?.uid)
                        editor.apply()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else{
                        Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show()
                    }
                }
            true
        }
        checkTheme()
        setBarColor()
    }

    private fun checkTheme() {
        /*when (Prefs(this).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
        }*/
        if (sp.getBoolean("darkMode", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        } else if (sp.getBoolean("darkMode", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }
    }

    fun setBarColor() {
        val actionBar: ActionBar? = supportActionBar
        val actionBarColor = sp.getString("toolbarColor", "#0F9D58")
        val colorDrawable = ColorDrawable(Color.parseColor(actionBarColor))
        actionBar?.setBackgroundDrawable(colorDrawable)
    }
}