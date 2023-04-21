package com.example.p1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class PersonListAdapter(context: Context, entries: MutableList<Person>, private val clickListener: (Person) -> Unit, private val longClickListener: (Person) -> Unit)
    : RecyclerView.Adapter<PersonListAdapter.ListViewHolder>() {

    var context: Context? = null
    var entries: MutableList<Person> = java.util.ArrayList()

    init {
        this.context = context
        this.entries = entries
    }

    fun updatePersonList(persons: ArrayList<Person>) {
        this.entries = persons
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.simple_card, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val person: Person = entries[position]
        holder.personName.text = person.name
        holder.personPhoto.setImageURI(person.photoPath.toUri())
        holder.personRating.rating = person.rating.toFloat()

        holder.personRowContainer.setOnClickListener {
            clickListener(person)
        }

        holder.personRowContainer.setOnLongClickListener {
            longClickListener(person)
            true
        }
    }

    override fun getItemCount() =  entries.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var personName: TextView
        var personRating: RatingBar
        var personPhoto: ImageView
        var personRowContainer: CardView

        init {
            personName = itemView.findViewById(R.id.name)
            personRating = itemView.findViewById(R.id.ratingBar)
            personPhoto = itemView.findViewById(R.id.imageView)
            personRowContainer = itemView.findViewById(R.id.personRowContainer)
        }
    }

    fun deletePerson(person: Person){
        entries.remove(person)
        notifyDataSetChanged()
    }
}