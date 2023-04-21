package com.example.p1

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioButton

class SortDialog constructor(context: Context) {

    private var sortBy = "ratting"

    private var context: Context
    private lateinit var rattingButton: RadioButton
    private lateinit var nameButton: RadioButton
    private lateinit var searchButton: Button

    init {
        this.context = context
    }

    fun showSortDialog() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_sort)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val lWindowParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(dialog.window!!.attributes)
        lWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lWindowParams

        rattingButton = dialog.findViewById(R.id.radioButton)
        nameButton = dialog.findViewById(R.id.radioButton2)
        searchButton = dialog.findViewById(R.id.searchButton)

        rattingButton.setOnClickListener {
            sortBy = "ratting"
        }

        nameButton.setOnClickListener {
            sortBy = "name"
        }

        searchButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}