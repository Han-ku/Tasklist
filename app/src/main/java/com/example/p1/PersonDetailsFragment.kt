package com.example.p1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.p1.databinding.FragmentPersonDetailsBinding


class PersonDetailsFragment : Fragment() {

    companion object {
        fun newInstance(person: Person): PersonDetailsFragment {
            val fragment = PersonDetailsFragment()
            val args = Bundle()
            args.putParcelable("person", person)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentPersonDetailsBinding? = null

    private val binding
        get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentPersonDetailsBinding.inflate(inflater, container, false)

        val person = arguments?.getParcelable<Person>("person")

        binding.editButton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainerView, NewPersonFragment.newInstance(person!!))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }

}