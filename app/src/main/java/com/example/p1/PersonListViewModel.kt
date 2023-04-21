package com.example.p1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonListViewModel : ViewModel() {

    var personList = MutableLiveData<ArrayList<Person>>()

    fun addPersons() {
        val persons: List<Person> = listOf(
            Person("", "John", 1),
            Person("", "Mary", 2),
            Person("", "Peter", 3)
        )

        personList.value = ArrayList(persons)
    }

    fun addPerson(photoPath: String, name: String, rating: Int) {
        val currentList: ArrayList<Person> = personList.value!!
        currentList.add(Person(photoPath, name, rating))
        personList.value = currentList

    }
}