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
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import com.example.p1.databinding.FragmentNewTaskBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.*

private const val GALLERY_REQUEST_CODE = 1
private const val FILE_REQUEST_CODE = 2

class NewTaskFragment : Fragment() {

    private var photoUri: Uri? = null
    private var fileUri: Uri? = null
    private var fileName: String? = null


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
            binding.nameEditText.setText(task.name)
            binding.descriptionEditText.setText(task.description)
            binding.ratingBar.rating = task.rating.toFloat()
            binding.deadlineTV.text = task.deadline
            if(getPhotoUri() != null) {
                photoUri = getPhotoUri()
                photoUri = task.photoPath.toUri()
                binding.photo.setImageURI(photoUri)
            }
            if(getFileUri() != null) {
                fileUri = getFileUri()
                fileName = getFileName()
                fileUri = task.filePath.toUri()
                binding.file.text = fileName
            }
            editMode = true
        }


        binding.deadlineTV.text = getFormattedDate(currentTimeMillis)

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
            // TODO delete photoPath
            photoUri = null
            binding.photo.visibility = View.GONE
            binding.takePhoto.visibility = View.VISIBLE
            binding.retakePhotoLayout.visibility = View.GONE
        }

        binding.file.setOnClickListener {
            // TODO check correct
            fileUri?.let {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(it, "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                try {
                    requireContext().startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "No PDF viewer installed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.getFile.setOnClickListener {
            openPackage()
        }

        binding.regetFile.setOnClickListener {
            openPackage()
        }

        binding.deleteFile.setOnClickListener {
            // TODO delete filePath
            binding.file.text = ""
            binding.file.visibility = View.GONE
            binding.getFile.visibility = View.VISIBLE
            binding.regetFileLayout.visibility = View.GONE
        }

        binding.saveButton.setOnClickListener {
            if(binding.nameEditText.text.toString() != "" && binding.descriptionEditText.text.toString() != "") {

                val standartRating: Int
                if(binding.ratingBar.rating.toInt() == 0) standartRating = 1
                else standartRating = binding.ratingBar.rating.toInt()

//                TODO photo is not added
//                TODO change title edit
                when(editMode){
                    true -> {
                        viewModel.editTask(task!!, photoUri.toString(), binding.file.text.toString(), binding.nameEditText.text.toString(), binding.descriptionEditText.text.toString(), standartRating, binding.deadlineTV.text.toString())
                    }
                    false -> {
                        viewModel.addTask(photoUri.toString(), binding.file.text.toString(), binding.nameEditText.text.toString(), binding.descriptionEditText.text.toString(), standartRating, binding.deadlineTV.text.toString())
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun openPackage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                GALLERY_REQUEST_CODE -> {
                    getPhotoVisibility()
                    photoUri = data?.data
//                    TODO photo rotation
                    if(photoUri != null) {
                        binding.photo.setImageBitmap(getBitmapFromUri(photoUri!!))
                        setPhotoUri(photoUri)
                    }
                    else Toast.makeText(requireContext(), "Failed to get image", Toast.LENGTH_SHORT).show()
                }
                FILE_REQUEST_CODE -> {
                    fileUri = data?.data
                    setFileUri(fileUri)
                    getFileVisibility()
                    fileUri?.let {
                        requireContext().contentResolver.query(fileUri!!, null, null, null, null)?.use { cursor ->
                            if (cursor.moveToFirst()) {
                                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                val pdfName = cursor.getString(nameIndex)
                                binding.file.text = pdfName
                                setFileName(pdfName)
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    fun getPhotoVisibility() {
        binding.photo.visibility = View.VISIBLE
        binding.takePhoto.visibility = View.GONE
        binding.retakePhotoLayout.visibility = View.VISIBLE
    }

    fun getFileVisibility() {
        binding.file.visibility = View.VISIBLE
        binding.getFile.visibility = View.GONE
        binding.regetFileLayout.visibility = View.VISIBLE
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
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
    private fun getPhotoUri(): Uri? {
        return photoUri
    }

    private fun setPhotoUri(path: Uri?) {
        photoUri = path
    }

    private fun getFileUri(): Uri? {
        return fileUri
    }

    private fun setFileUri(path: Uri?) {
        fileUri = path
    }

    private fun getFileName(): String? {
        return fileName
    }

    private fun setFileName(name: String?) {
        fileName = name
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

