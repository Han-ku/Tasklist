package com.example.p1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.example.p1.databinding.FragmentTaskDetailsBinding

class TaskDetailsFragment : Fragment() {

    var filePath: Uri? = null
    companion object {
        fun newInstance(task: Task): TaskDetailsFragment {
            val fragment = TaskDetailsFragment()
            val args = Bundle()
            args.putParcelable("task", task)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentTaskDetailsBinding? = null

    private val binding
        get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)

        val task = arguments?.getParcelable<Task>("task")

        binding.apply {
            if (task != null) {
                checkFile(task.filePath)
                file.text = getFileNameFromUri(task.filePath.toUri())
                filePath = task.filePath.toUri()
                name.text = task.name
                description.text = task.description
                ratingBar.rating = task.rating.toFloat()
                deadline.text = task.deadline
            }
        }

        binding.editButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, NewTaskFragment.newInstance(task!!))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.file.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(filePath, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                requireContext().startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No PDF viewer app found", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun checkFile(filePath: String) {
        if(filePath == "" || filePath == resources.getString(R.string.add_file) || filePath == "null") {
            binding.file.visibility = View.GONE
            binding.fileTitle.visibility = View.GONE
        } else {
            binding.file.visibility = View.VISIBLE
            binding.fileTitle.visibility = View.VISIBLE
        }
    }

    fun getFileNameFromUri(uri: Uri): String? {
        val documentFile = DocumentFile.fromSingleUri(requireContext(), uri)
        return documentFile?.name
    }
}