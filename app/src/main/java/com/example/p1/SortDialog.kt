package com.example.p1

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioButton
import androidx.fragment.app.Fragment

class SortDialog constructor(context: Context) {

    private var sortBy = "latest"

    private var context: Context
    private lateinit var ratingButton: RadioButton
    private lateinit var nameButton: RadioButton
    private lateinit var latestButton: RadioButton
    private lateinit var searchButton: Button

    init {
        this.context = context
    }

    fun showSortDialog(fragment: ListFragment) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_sort)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val lWindowParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(dialog.window!!.attributes)
        lWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lWindowParams

        ratingButton = dialog.findViewById(R.id.radioButton)
        nameButton = dialog.findViewById(R.id.radioButton2)
        latestButton = dialog.findViewById(R.id.radioButton3)
        searchButton = dialog.findViewById(R.id.searchButton)

        ratingButton.setOnClickListener {
            sortBy = "rating"
        }

        nameButton.setOnClickListener {
            sortBy = "name"
        }

        latestButton.setOnClickListener {
            sortBy = "latest"
        }

        searchButton.setOnClickListener {
            fragment.sortTaskList(sortBy)
            dialog.dismiss()
        }
        dialog.show()
    }
}