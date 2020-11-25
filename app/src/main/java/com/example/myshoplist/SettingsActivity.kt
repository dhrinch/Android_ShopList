package com.example.myshoplist

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.colorpicker.*
import kotlinx.android.synthetic.main.settings_activity.*


class SettingsActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        sp = getSharedPreferences("Prefs", MODE_PRIVATE)
        editor = sp.edit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //val mToolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        val sw1 = findViewById<Switch>(R.id.theme_switch)
        if (sp.getBoolean("darkMode", true)) {
            sw1.isChecked = true
            /*if (Prefs(this).darkMode == 1) {
            sw1.isChecked = true*/
        }
        sw1?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                //Prefs(this).darkMode = 1
                delegate.applyDayNight()
                editor.putBoolean("darkMode", true)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                //Prefs(this).darkMode = 0
                delegate.applyDayNight()
                editor.putBoolean("darkMode", false)
                editor.apply()
            }
        }

        color_well.setOnClickListener {
            val intent = Intent(this, colorPicker::class.java)
            startActivity(intent)
        }

        /*val intent = intent
        val color = intent.getStringExtra("color")
        val colorFromIntent = ColorDrawable(Color.parseColor(color))
        actionBar?.setBackgroundDrawable(colorFromIntent)*/

       /* mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        mToolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }*/

        /*override fun onStart(){
        super.onStart()
        sp.getBoolean("DarkTheme", sw1.isChecked)
    }*/

        /*override fun onStop() {
        super.onStop()
        editor.apply()
    }*/

        //fun Return(context: Context) {
        /*val intent = Intent(Settings.ACTION_SETTINGS)*/
        //val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("DarkTheme", sw1.isChecked
        //startActivity(intent)
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //context.startActivity(intent)
        //}



        /*actionBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        actionBar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }*/
    }

    // Setting ActionBar color
    fun setBarColor() {
        val actionBar: ActionBar? = supportActionBar
        val color = sp.getString("toolbarColor", "#0F9D58")
        val colorDrawable = ColorDrawable(Color.parseColor(color))
        actionBar?.setBackgroundDrawable(colorDrawable)
        color_well.setBackgroundColor(Color.parseColor(color))
    }

    override fun onStart(){
        super.onStart()
        setBarColor()
    }
}

/*class Prefs(context: Context?) {

    companion object {
        private const val DARK_STATUS = "Dark mode"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var darkMode = preferences.getInt(DARK_STATUS, 0)
        set(value) = preferences.edit().putInt(DARK_STATUS, value).apply()
}*/

/*class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}*/

