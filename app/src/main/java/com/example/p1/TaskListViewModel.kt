package com.example.p1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel() {

    var taskList = MutableLiveData<ArrayList<Task>>()

    fun addTasks() {
        val tasks: List<Task> = listOf(
            Task("", "Peter", "scfvgbhjnxdrcftvgybhunjimk", 1, "13-05-2023"),
            Task("", "John", "asexdcfvgbhnjmkv bnjkm", 3, "01-07-2023"),
            Task("", "Mary", "xdcfvgbhnjkl;,kjbhvgcfcf  fgvbhjn", 2, "05-06-2024")
        )

        taskList.value = ArrayList(tasks)
    }


    fun addTask(photoPath: String, name: String, description: String, rating: Int, deadline: String) {
        val currentList: ArrayList<Task> = taskList.value!!
        currentList.add(Task(photoPath, name, description, rating, deadline))
        taskList.value = currentList

    }

    fun deleteTask(task: Task){
        taskList.value!!.remove(task)
    }
}