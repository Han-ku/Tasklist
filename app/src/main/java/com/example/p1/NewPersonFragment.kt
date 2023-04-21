package com.example.p1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.p1.databinding.FragmentNewPersonBinding

private const val GALLERY_REQUEST_CODE = 1

class NewPersonFragment : Fragment() {

    private val viewModel: PersonListViewModel by activityViewModels()

    companion object {
        fun newInstance(person: Person): NewPersonFragment {
            val fragment = NewPersonFragment()
            val args = Bundle()
            args.putParcelable("person", person)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentNewPersonBinding? = null
    private val binding
        get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPersonBinding.inflate(inflater, container, false)

        val person = arguments?.getParcelable<Person>("person")
        
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
            if(binding.nameEditText.text.toString() != "" && binding.ratingBar.rating.toInt() != 0) {
                viewModel.addPerson("", binding.nameEditText.text.toString(), binding.ratingBar.rating.toInt() )
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

        if(binding.ratingBar.numStars != 0) binding.ratingError.visibility = View.VISIBLE
        else binding.ratingError.visibility = View.GONE
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
}