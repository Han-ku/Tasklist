package com.example.p1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.p1.databinding.FragmentNewTaskBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.*


private const val GALLERY_REQUEST_CODE = 1

class NewTaskFragment : Fragment() {

    private val viewModel: TaskListViewModel by activityViewModels()

    companion object {
        fun newInstance(task: Task): NewTaskFragment {
            val fragment = NewTaskFragment()
            val args = Bundle()
            args.putParcelable("task", task)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentNewTaskBinding? = null
    private val binding
        get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentNewTaskBinding.inflate(inflater, container, false)

        val task = arguments?.getParcelable<Task>("task")

        binding.deadlineLayoutTV.setOnClickListener {
            showDatePickerDialog()
        }

        binding.takePhoto.setOnClickListener {
            openGallery()
        }

        binding.retakePhoto.setOnClickListener {
            openGallery()
        }

        binding.deletePhoto.setOnClickListener {
            binding.photo.setImageResource(R.drawable.outline_add_a_photo_24)
        }

        binding.saveButton.setOnClickListener {
            if(binding.nameEditText.text.toString() != "" && binding.descriptionEditText.text.toString() != "" && binding.deadlineTV.text.toString() != "") {

                val standartRating: Int
                if(binding.ratingBar.rating.toInt() == 0) standartRating = 1
                else standartRating = binding.ratingBar.rating.toInt()

                viewModel.addTask("", binding.nameEditText.text.toString(), binding.descriptionEditText.text.toString(), standartRating, binding.deadlineTV.text.toString())
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, ListFragment())
                transaction.addToBackStack(null)
                transaction.commit()

            } else {
                showError()
            }
        }
        return binding.root
    }

    fun showError() {
        if(binding.nameEditText.text.toString() == "") binding.nameError.visibility = View.VISIBLE
        else binding.nameError.visibility = View.GONE

        if(binding.descriptionEditText.text.toString() == "") binding.descriptionError.visibility = View.VISIBLE
        else binding.descriptionError.visibility = View.GONE

        if(binding.deadlineTV.text.toString() == "") binding.deadlineError.visibility = View.VISIBLE
        else binding.deadlineError.visibility = View.GONE
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                GALLERY_REQUEST_CODE -> {
                    getPhoto()
//                    TODO find how save photopath
                    binding.photo.load(data?.data)
                }
            }
        } else {
            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    fun getPhoto() {
        binding.photo.visibility = View.VISIBLE
        binding.photo.rotation = 0f
        binding.takePhoto.visibility = View.GONE
        binding.retakePhotoLayout.visibility = View.VISIBLE
    }

    private fun showDatePickerDialog() {

        val picker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        picker.show(requireFragmentManager(), "tag")

        picker.addOnPositiveButtonClickListener {
            binding.deadlineTV.text = "${getFormattedDate(it)}"
            picker.dismiss()
        }

        picker.addOnCancelListener {
            picker.dismiss()
        }
    }

    private fun getFormattedDate(timestamp: Long) : String {
        val localDateTime = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
        val year = localDateTime.year.toString()
        val month = localDateTime.month.number.toString().padStart(2, '0')
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val formattedDate = "$day-$month-$year"

        return formattedDate
    }
}