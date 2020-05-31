package com.example.myapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*

class Tasks(val title:String, val description:String) {
    constructor():this("", "")
}

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(tasks: Tasks?){
        with(tasks!!){
            itemView.title.text = tasks.title
            itemView.description.text = tasks.description
        }
    }
}