package com.example.p1

import androidx.lifecycle.ViewModel

class PersonListViewModel : ViewModel() {
    fun addPersons(adapter: PersonListAdapter) {
        val persons: List<Person> = listOf(
            Person( "","John", 1),
            Person("", "Mary", 2),
            Person("","Peter", 3)
        )

        val currentList: MutableList<Person> = adapter.entries
        currentList.addAll(persons)
        adapter.updatePersonList(ArrayList(persons))
    }

    fun addPerson(photoPath: String, name: String, rating: Int) {
        Person(photoPath, name, rating)
    }
}