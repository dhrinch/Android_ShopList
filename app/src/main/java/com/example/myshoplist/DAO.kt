package com.example.myshoplist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    /*@Query("SELECT * from shop_list_table")
    fun getAll(): MutableList<ShoppingListItem>

    @Insert
    fun insert(item: ShoppingListItem) : Long

    @Query("DELETE FROM shop_list_table WHERE id = :itemId")
    fun delete(itemId: Int)*/

    @Query("SELECT * from shop_table")
    fun getAll(): MutableList<ShopItem>

    @Insert
    fun insert(item: ShopItem) : Long

    @Query("DELETE FROM shop_table WHERE id = :shopId")
    fun delete(shopId: Int)

}