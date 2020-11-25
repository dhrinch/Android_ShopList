package com.example.myshoplist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingListItem::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun DAO() : Dao
}