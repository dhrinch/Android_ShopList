package com.example.myshoplist

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_add_shop.view.*

class AddShopDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val customView = LayoutInflater.from(context).inflate(R.layout.dialog_add_shop, null)
            val address = arguments?.getString("address", "none")
            val lat = arguments?.getString("lat", "none")
            val long = arguments?.getString("long", "none")
            val radius = arguments?.getString("radius", "none")

            customView.tv_shopAddress.text = address
            customView.tv_shopLatitude.text = lat
            customView.tv_shopLongitude.text = long
            customView.et_fenceRadius?.setText(radius)
            val builder = AlertDialog.Builder(it)

            builder.setView(customView)
            builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_ok) { dialog, id ->
                    val name = customView.et_shopName.text.toString()
                    val desc = customView.et_shopDescription.text.toString()
                    val radius = customView.et_fenceRadius.text.toString().toFloat()
                    val coords_lat = lat!!.toDouble()//customView.tv_shopLatitude.text.toString().toDouble()
                    val coords_long = long!!.toDouble()//customView.tv_shopLongitude.text.toString().toDouble()
                    val item = address?.let { it1 ->
                        ShopItem(0, name, desc,
                            it1, radius, coords_lat, coords_long)
                    }

                    val bundle = Bundle()
                    bundle.putString("radius", radius.toString())
                    val intent = Intent().putExtras(bundle)
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                    if (item != null) {
                        mListener.onDialogPositiveClick(item)
                    }

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