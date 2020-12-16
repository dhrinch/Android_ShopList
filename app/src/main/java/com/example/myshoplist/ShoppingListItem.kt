package com.example.myshoplist

/*
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_list_table")*/
data class ShoppingListItem(
        //@PrimaryKey(autoGenerate = true)
        var id: Int,
        var name: String,
        var count: Int,
        var price: Double
)

/*class ShoppingListItem{
        companion object Factory{
                fun create(): ShoppingListItem = ShoppingListItem()
        }

        var id: String? = null
        var name: String? = null
        var count: Int = 0
        var price: Double = 0.0
        //var done: Boolean? = false
}*/
