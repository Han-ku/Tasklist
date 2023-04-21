package com.example.p1

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p1.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get()=_binding!!

    private lateinit var adapter: PersonListAdapter
    private val viewModel: PersonListViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentListBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(this)[PersonListViewModel::class.java]
        setHasOptionsMenu(true)

        if(viewModel.personList.value == null){
            viewModel.addPersons()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PersonListAdapter(requireContext(), mutableListOf(),
            clickListener = {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, PersonDetailsFragment.newInstance(it))
                transaction.addToBackStack(null)
                transaction.commit() },
            longClickListener = {
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Delete person").setMessage("Are you sure?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    //adapter.deletePerson(it)
                    viewModel.deletePerson(it)
                    adapter.notifyDataSetChanged()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
        })

        viewModel.personList.observe(requireActivity()) {
            adapter.updatePersonList(it)
        }

        binding.amountTextView.text = viewModel.personList.value!!.size.toString()
        binding.recyclerView.adapter = adapter

        binding.floatingActionButton2.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, NewPersonFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }


    fun sortPersonList(sortBy: String){
        var sortedList = ArrayList<Person>()

        when(sortBy){
            "rating" -> {
                ArrayList(viewModel.personList.value!!.sortedBy { it.rating }).let { adapter.updatePersonList(it) }
            }
            "name" -> {
                ArrayList(viewModel.personList.value!!.sortedBy { it.name }).let { adapter.updatePersonList(it) }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sortBy -> {
                SortDialog(requireContext()).showSortDialog(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}