package com.example.p1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.example.p1.databinding.FragmentTaskDetailsBinding

class TaskDetailsFragment : Fragment() {

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
        // Inflate the layout for this fragment
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)

        val task = arguments?.getParcelable<Task>("task")


        binding.apply {
            if (task != null) {
                checkPhoto(task.photoPath)
                if(task.photoPath != "") {
                    photo.setImageURI(task.photoPath.toUri())
                }
                name.text = task.name
                description.text = task.description
                ratingBar.rating = task.rating.toFloat()
                deadline.text = task.deadline
            }
        }

        binding.editButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainerView, NewTaskFragment.newInstance(task!!))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }

    fun checkPhoto(photoPath: String) {
        if(photoPath == "") binding.photo.visibility = View.GONE
        else binding.photo.visibility = View.VISIBLE
    }
}