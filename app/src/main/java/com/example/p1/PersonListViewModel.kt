package com.example.p1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonListViewModel : ViewModel() {

    var personList = MutableLiveData<ArrayList<Person>>()

    fun addPersons() {
        val persons: List<Person> = listOf(
            Person("", "Peter", 1),
            Person("", "John", 3),
            Person("", "Mary", 2)
        )

        personList.value = ArrayList(persons)
    }

    fun addPerson(photoPath: String, name: String, rating: Int) {
        val currentList: ArrayList<Person> = personList.value!!
        currentList.add(Person(photoPath, name, rating))
        personList.value = currentList

    }

    fun deletePerson(person: Person){
        personList.value!!.remove(person)
    }
}