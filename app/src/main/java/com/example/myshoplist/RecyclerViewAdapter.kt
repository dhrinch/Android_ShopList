package com.example.myshoplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class RecyclerViewAdapter (var shopList: MutableList<ShoppingListItem>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = shopList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ShoppingListItem = shopList[position]
        holder.nameTextView.text = item.name
        holder.countTextView.text = item.count.toString()
        holder.priceTextView.text = item.price.toString()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.tv_ShopName
        val countTextView: TextView = view.tv_ShopDesc
        val priceTextView: TextView = view.tv_ShopCoordsLat
    }

    fun update(newList: MutableList<ShoppingListItem>) {
        shopList = newList
        notifyDataSetChanged()
    }
}