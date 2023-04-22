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

class TaskListAdapter(context: Context, entries: MutableList<Task>, private val clickListener: (Task) -> Unit, private val longClickListener: (Task) -> Unit)
    : RecyclerView.Adapter<TaskListAdapter.ListViewHolder>() {

    var context: Context? = null
    var entries: MutableList<Task> = java.util.ArrayList()

    init {
        this.context = context
        this.entries = entries
    }

    fun updateTaskList(tasks: ArrayList<Task>) {
        this.entries = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.simple_card, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val task: Task = entries[position]
        holder.taskName.text = task.name
//        holder.taskDescription.text = task.description
        holder.taskPhoto.setImageURI(task.photoPath.toUri())
        holder.taskRating.rating = task.rating.toFloat()
        holder.taskDeadline.text = task.deadline

        holder.taskRowContainer.setOnClickListener {
            clickListener(task)
        }

        holder.taskRowContainer.setOnLongClickListener {
            longClickListener(task)
            true
        }
    }

    override fun getItemCount() =  entries.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskName: TextView
//        var taskDescription: TextView
        var taskRating: RatingBar
        var taskPhoto: ImageView
        var taskDeadline: TextView
        var taskRowContainer: CardView

        init {
            taskName = itemView.findViewById(R.id.name)
//            taskDescription = itemView.findViewById(R.id.description)
            taskRating = itemView.findViewById(R.id.ratingBar)
            taskPhoto = itemView.findViewById(R.id.imageView)
            taskDeadline = itemView.findViewById(R.id.deadline)
            taskRowContainer = itemView.findViewById(R.id.taskRowContainer)
        }
    }

//    fun deleteTask(task: Task){
//        entries.remove(task)
//        notifyDataSetChanged()
//    }
}