package com.example.myshoplist

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ShoppingListItem::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun DAO() : Dao

    companion object {
        private var INSTANCE: RoomDB? = null
        private const val DB_NAME = "shops_db"

        fun getDatabase(context: Context): RoomDB {
            if (INSTANCE == null) {
                synchronized(RoomDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, RoomDB::class.java, DB_NAME)
                            //.allowMainThreadQueries() // Uncomment if you don't want to use RxJava or coroutines just yet (blocks UI thread)
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    Log.d("ShopDatabase", "populating with data...")
                                    //GlobalScope.launch(Dispatchers.IO) { rePopulateDb(INSTANCE) }
                                }
                            }).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}