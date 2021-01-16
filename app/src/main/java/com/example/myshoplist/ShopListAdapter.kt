package com.example.myshoplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.shops_recycler_layout.view.*

class ShopListAdapter (var shops: MutableList<ShopItem>) : RecyclerView.Adapter<ShopListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.shops_recycler_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = shops.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ShopItem = shops[position]
        holder.nameTextView.text = item.name
        holder.descTextView.text = item.desc
        holder.radiusTextView.text = item.radius.toString()
        holder.longTextView.text = item.coords_long.toString()
        holder.latTextView.text = item.coords_lat.toString()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.tv_name
        val descTextView: TextView = view.tv_desc
        val radiusTextView: TextView = view.tv_fenceRad
        val longTextView: TextView = view.tv_shopLong
        val latTextView: TextView = view.tv_shopLat

    }

    fun update(newList: MutableList<ShopItem>) {
        shops = newList
        notifyDataSetChanged()
    }
}