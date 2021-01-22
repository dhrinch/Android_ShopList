package com.example.myshoplist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_add_item.view.*

class DialogFragmentItem : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val customView = LayoutInflater.from(context).inflate(R.layout.dialog_add_item, null)

            val builder = AlertDialog.Builder(it)
            builder.setView(customView)
            builder.setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_message)
                    .setPositiveButton(R.string.dialog_ok) { dialog, id ->
                        val name = customView.editItem.text.toString()
                        val count = customView.editCount.text.toString().toInt()
                        val price = customView.editPrice.text.toString().toDouble()
                        val item = ShoppingListItem(0, name, count, price)
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
        fun onDialogPositiveClick(item: ShoppingListItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as AddDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement AddDialogListener"))
        }
    }
}