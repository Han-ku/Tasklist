package com.example.p1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel() {

    var taskList = MutableLiveData<ArrayList<Task>>()

    fun addTasks() {
        val tasks: List<Task> = listOf(
            Task("1", "", "Peter", "scfvgbhjnxdrcftvgybhunjimk", 1, "13-05-2023"),
            Task("2", "", "John", "asexdcfvgbhnjmkv bnjkm", 3, "01-07-2023"),
            Task("3", "", "Mary", "xdcfvgbhnjkl;,kjbhvgcfcf  fgvbhjn", 2, "05-06-2024")
        )

        taskList.value = ArrayList(tasks)
    }


    fun addTask(photoPath: String, name: String, description: String, rating: Int, deadline: String) {
        val currentList: ArrayList<Task> = taskList.value!!
        //бред но пока так
        val id = name+rating+deadline+(0..50).random().toString()
        currentList.add(Task(id,photoPath, name, description, rating, deadline))
        taskList.value = currentList
    }

    fun deleteTask(task: Task){
        taskList.value!!.remove(task)
    }

    fun editTask(task: Task, photoPath: String, name: String, description: String, rating: Int, deadline: String) {
        taskList.value!!.forEach {
            if (it.id == task.id){
                it.name = name
                it.photoPath = photoPath
                it.description = description
                it.rating = rating
                it.deadline = deadline
            }
        }
    }
}