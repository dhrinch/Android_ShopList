package com.example.myshoplist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_table")
data class ShopItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var desc: String,
    var address: String,
    var radius: Float,
    var coords_lat: Double,
    var coords_long: Double
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