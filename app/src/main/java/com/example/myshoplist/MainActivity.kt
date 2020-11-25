package com.example.myshoplist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), DialogFragmentItem.AddDialogListener {

    private var shopList: MutableList<ShoppingListItem> = ArrayList()
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var db: RoomDB
    private lateinit var sp: SharedPreferences

    override fun onStart(){
        super.onStart()
        setBarColor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        sp = getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(shopList)
        recyclerView.adapter = adapter

        db = Room.databaseBuilder(applicationContext, RoomDB::class.java, "sl_db").build()
        loadShopListItems()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val dialog = DialogFragmentItem()
            dialog.show(supportFragmentManager, "AskNewItemDialogFragment")
        }

        initSwipe()
        checkTheme()
    }

    private fun initSwipe() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val handler = Handler(Handler.Callback {
                    Toast.makeText(
                        applicationContext,
                        it.data.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                    adapter.update(shopList)
                    true
                })
                val id = shopList[position].id
                shopList.removeAt(position)
                Thread(Runnable {
                    db.DAO().delete(id)
                    val message = Message.obtain()
                    message.data.putString("message", "Item removed")
                    handler.sendMessage(message)
                }).start()
            }

            override fun onMove(
                p0: RecyclerView,
                p1: RecyclerView.ViewHolder,
                p2: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun loadShopListItems() {
        val handler = Handler(Handler.Callback {
            Toast.makeText(applicationContext, it.data.getString("message"), Toast.LENGTH_SHORT)
                .show()
            adapter.update(shopList)
            true
        })
        Thread(Runnable {
            shopList = db.DAO().getAll()
            val message = Message.obtain()
            if (shopList.size > 0)
                message.data.putString("message", "List loaded")
            else
                message.data.putString("message", "List is empty")
            handler.sendMessage(message)
        }).start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if(id == R.id.action_settings) Setting(this)
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun Setting(context: Context) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    override fun onDialogPositiveClick(item: ShoppingListItem) {
        val handler = Handler(Handler.Callback {
            Toast.makeText(applicationContext, it.data.getString("message"), Toast.LENGTH_SHORT)
                .show()
            adapter.update(shopList)
            true
        })
        Thread(Runnable {
            val id = db.DAO().insert(item)
            item.id = id.toInt()
            shopList.add(item)
            val message = Message.obtain()
            message.data.putString("message", "Item added to list")
            handler.sendMessage(message)
        }).start()
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

    /*override fun onStop() {
        super.onStop()
        //editor.putBoolean(intent.DarkTheme)
        editor.apply()
    }*/

    /*fun click(view: View) {
        if (cb_isBought.isChecked) {
            val intent1 = Intent(this, SecondaryActivity::class.java)
            intent1.putExtra("str1", editText1.text.toString())
            startActivity(intent1)
        } else {
        }
    }*/
}