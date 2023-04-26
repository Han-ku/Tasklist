package com.example.p1

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.activityViewModels
import com.example.p1.databinding.FragmentNewTaskBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.*

private const val FILE_REQUEST_CODE = 2

class NewTaskFragment : Fragment() {

    private var fileUri: Uri? = null

    private val viewModel: TaskListViewModel by activityViewModels()
    private var editMode : Boolean = false

    val currentTimeMillis = Clock.System.now().toEpochMilliseconds()

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

        if(task != null){
            binding.title.text = resources.getString(R.string.edit_task)
            binding.nameEditText.setText(task.name)
            binding.descriptionEditText.setText(task.description)
            binding.ratingBar.rating = task.rating.toFloat()
            binding.deadlineTV.text = task.deadline
            if(task.filePath != "" && task.filePath != resources.getString(R.string.add_file) && task.filePath != "null") {
                binding.fileTV.text = viewModel.getFileNameFromUri(requireContext(), task.filePath.toUri())
                getFileVisibility()
            }
            editMode = true
        }

        if(binding.deadlineTV.text == "") {
            binding.deadlineTV.text = getFormattedDate(currentTimeMillis)
        }

        binding.deadlineLayoutTV.setOnClickListener {
            showDatePickerDialog()
        }


        binding.fileTV.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(fileUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                requireContext().startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No PDF viewer app found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fileLayoutTV.setOnClickListener {
            openPackage()
        }

        binding.updateFile.setOnClickListener {
            fileUri = null
            openPackage()
        }

        binding.deleteFile.setOnClickListener {
            binding.fileTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            fileUri = null
            binding.fileTV.text = resources.getString(R.string.add_file)
            binding.fileImageTV.visibility = View.VISIBLE
            binding.updateFileLayout.visibility = View.GONE
        }


        binding.saveButton.setOnClickListener {
            if(binding.nameEditText.text.toString() != "" && binding.descriptionEditText.text.toString() != "") {

                val standartRating: Int
                if(binding.ratingBar.rating.toInt() == 0) standartRating = 1
                else standartRating = binding.ratingBar.rating.toInt()

                when(editMode){
                    true -> {
                        viewModel.editTask(task!!, fileUri.toString(), binding.nameEditText.text.toString(), binding.descriptionEditText.text.toString(), standartRating, binding.deadlineTV.text.toString())
                    }
                    false -> {
                        viewModel.addTask(fileUri.toString(), binding.nameEditText.text.toString(), binding.descriptionEditText.text.toString(), standartRating, binding.deadlineTV.text.toString())
                    }
                }
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
    }

    private fun openPackage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                FILE_REQUEST_CODE -> {
                    fileUri = data?.data
                    fileUri?.let {
                        requireContext().contentResolver.query(fileUri!!, null, null, null, null)?.use { cursor ->
                            if (cursor.moveToFirst()) {
                                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                val pdfName = cursor.getString(nameIndex)
                                binding.fileTV.text = pdfName
                                getFileVisibility()
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    fun getFileVisibility() {
        if(binding.fileTV.text != resources.getString(R.string.add_file) && binding.fileTV.text != null && binding.fileTV.text != "") {
            binding.fileTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.chestnut))
            binding.fileImageTV.visibility = View.GONE
            binding.updateFileLayout.visibility = View.VISIBLE
        } else {
            binding.fileTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            binding.fileImageTV.visibility = View.VISIBLE
            binding.updateFileLayout.visibility = View.GONE
        }
    }

    private fun showDatePickerDialog() {
        val picker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        picker.show(requireFragmentManager(), "tag")

        picker.addOnPositiveButtonClickListener {
            if(it < currentTimeMillis) {
                Toast.makeText(requireContext(), "Selected date is before current date", Toast.LENGTH_SHORT).show()
                return@addOnPositiveButtonClickListener
            }

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

