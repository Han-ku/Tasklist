package com.example.p1

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.datetime.Clock

class TaskListViewModel : ViewModel() {

    var taskList = MutableLiveData<ArrayList<Task>>()

    val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"
    fun addTasks() {
        val tasks: List<Task> = listOf(
            Task("1",  "", "Peter", description, 1, "13-05-2023"),
            Task("2",  "", "John", description, 3, "01-07-2023"),
            Task("3", "", "Mary", description, 2, "05-06-2024")
        )
        taskList.value = ArrayList(tasks)
    }


    fun addTask(filePath: String, name: String, description: String, rating: Int, deadline: String) {
        val currentList: ArrayList<Task> = taskList.value!!
        val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
        val id = name + rating + deadline + currentTimeMillis
        currentList.add(Task(id, filePath, name, description, rating, deadline))
        taskList.value = currentList
    }

    fun deleteTask(task: Task) : ArrayList<Task>{
        taskList.value!!.remove(task)
        return taskList.value!!
    }

    fun editTask(task: Task, filePath: String, name: String, description: String, rating: Int, deadline: String) {
        taskList.value!!.forEach {
            if (it.id == task.id){
                it.name = name
                it.filePath = filePath
                it.description = description
                it.rating = rating
                it.deadline = deadline
            }
        }
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        return documentFile?.name
    }
}