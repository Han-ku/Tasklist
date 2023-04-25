package com.example.p1

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p1.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get()=_binding!!

    private lateinit var adapter: TaskListAdapter
    private val viewModel: TaskListViewModel by activityViewModels()

    private lateinit var delete: TextView
    private lateinit var cancel: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        if(viewModel.taskList.value == null){
            viewModel.addTasks()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TaskListAdapter(requireContext(), mutableListOf(),
            clickListener = {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, TaskDetailsFragment.newInstance(it))
                transaction.addToBackStack(null)
                transaction.commit() },
            longClickListener = {
                val dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.dialog_delete)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                delete = dialog.findViewById(R.id.delete)
                cancel = dialog.findViewById(R.id.cancel)

                delete.setOnClickListener { which ->
                    adapter.updateTaskList(viewModel.deleteTask(it))
                    dialog.dismiss()
                }

                cancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
        })

        viewModel.taskList.observe(requireActivity()) {
            sortTaskList("latest")
        }

        binding.amountTextView.text = viewModel.taskList.value!!.size.toString()
        binding.recyclerView.adapter = adapter

        binding.floatingActionButton2.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, NewTaskFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding.root
    }


    fun sortTaskList(sortBy: String){
        when(sortBy){
            "rating" -> {
                ArrayList(viewModel.taskList.value!!.sortedBy { it.rating }).let { adapter.updateTaskList(it) }
            }
            "name" -> {
                ArrayList(viewModel.taskList.value!!.sortedBy { it.name.uppercase() }).let { adapter.updateTaskList(it) }
            }
            "latest" -> {
                ArrayList(viewModel.taskList.value!!.sortedByDescending { it.dateAdded }).let { adapter.updateTaskList(it) }
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