package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.completed_dialog.*

class Completed : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)

        val uid = FirebaseAuth.getInstance().uid

        val query = FirebaseDatabase.getInstance().reference.child(uid!!).child("completed")

        val options = FirebaseRecyclerOptions.Builder<Tasks>()
            .setQuery(query, Tasks::class.java)
            .setLifecycleOwner(this)
            .build()

        val mAdapter = object : FirebaseRecyclerAdapter<Tasks, TaskViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
                return TaskViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
                )
            }

            override fun onBindViewHolder(holder: TaskViewHolder, position: Int, model: Tasks) {
                holder.bind(model)
                holder.itemView.setOnClickListener(View.OnClickListener {
                    val child = getRef(position).key
                    //Toast.makeText(activity, child, Toast.LENGTH_SHORT).show()
                    val dialog = Dialog(this@Completed)
                    dialog.setContentView(R.layout.completed_dialog)
                    dialog.dialog_title.text = model.title
                    dialog.dialog_description.text = model.description

                    dialog.delete.setOnClickListener {
                        FirebaseDatabase.getInstance().reference.child(uid!!).child("completed")
                            .child(child!!)
                            .removeValue()
                        dialog.dismiss()
                    }
                    dialog.show()
                })
            }

        }


        val recyclerView = findViewById<RecyclerView>(R.id.tasks)
            .apply {
                layoutManager = LinearLayoutManager(this@Completed)
                adapter = mAdapter
            }
    }
}
