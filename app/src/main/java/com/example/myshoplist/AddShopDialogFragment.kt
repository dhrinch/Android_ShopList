package com.example.myshoplist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_add_item.view.et_fenceRadius
import kotlinx.android.synthetic.main.dialog_add_item.view.et_shopDescription
import kotlinx.android.synthetic.main.dialog_add_item.view.et_shopName
import kotlinx.android.synthetic.main.dialog_add_shop.*
import kotlinx.android.synthetic.main.dialog_add_shop.view.*
import kotlinx.android.synthetic.main.shops_recycler_layout.*

class AddShopDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val customView = LayoutInflater.from(context).inflate(R.layout.dialog_add_shop, null)

            val bundle = arguments
            //et_fenceRadius.setText(bundle?.getFloat("radius", 0F).toString())
            /*et_shopLat.setText(bundle?.getDouble("lat", 0.0).toString())
            et_shopLong.setText(bundle?.getDouble("long", 0.0).toString())*/
            val builder = AlertDialog.Builder(it)
            builder.setView(customView)
            builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_ok) { dialog, id ->
                    val name = customView.et_shopName.text.toString()
                    val desc = customView.et_shopDescription.text.toString()
                    val coords_lat = customView.et_shopLat.text.toString().toDouble()
                    val coords_long = customView.et_shopLong.text.toString().toDouble()
                    val radius = customView.et_fenceRadius.text.toString().toFloat()
                    val item = ShopItem(0, name, desc, radius, coords_lat, coords_long)
                    mListener.onDialogPositiveClick(item)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_cancel) { dialog, id ->
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private lateinit var mListener: AddDialogListener

    interface AddDialogListener {
        fun onDialogPositiveClick(item: ShopItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as AddDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement AddDialogListener")
            )
        }
    }
}