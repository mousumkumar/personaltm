package com.example.myapplication

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.assigned_dialog.*
import kotlinx.android.synthetic.main.assigned_dialog.delete
import kotlinx.android.synthetic.main.assigned_dialog.dialog_description
import kotlinx.android.synthetic.main.assigned_dialog.dialog_title
import kotlinx.android.synthetic.main.incomplete_dialog.*

class Incomplete : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomplete)

        val uid = FirebaseAuth.getInstance().uid

        val query = FirebaseDatabase.getInstance().reference.child(uid!!).child("incomplete")

        val options = FirebaseRecyclerOptions.Builder<Tasks> ()
            .setQuery(query, Tasks::class.java)
            .setLifecycleOwner(this)
            .build()

        val mAdapter = object : FirebaseRecyclerAdapter<Tasks, TaskViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
                return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false))
            }

            override fun onBindViewHolder(holder: TaskViewHolder, position: Int, model: Tasks) {
                holder.bind(model)
                holder.itemView.setOnClickListener(View.OnClickListener {
                    val child = getRef(position).key
                    //Toast.makeText(activity, child, Toast.LENGTH_SHORT).show()
                    val dialog = Dialog(this@Incomplete)
                    dialog.setContentView(R.layout.incomplete_dialog)
                    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    dialog.dialog_title.text = model.title
                    dialog.dialog_description.text = model.description
                    dialog.move_to_assigned.setOnClickListener {
                        FirebaseDatabase.getInstance().reference.child(uid!!).child("assigned").push()
                            .setValue(model)
                            .addOnCompleteListener {
                                FirebaseDatabase.getInstance().reference.child(uid).child("incomplete").child(child!!)
                                    .removeValue()
                            }
                        dialog.dismiss()
                    }
                    dialog.delete.setOnClickListener {
                        FirebaseDatabase.getInstance().reference.child(uid).child("incomplete").child(child!!)
                            .removeValue()
                        dialog.dismiss()
                    }
                    dialog.show()
                })
            }

        }


        val recyclerView = findViewById<RecyclerView>(R.id.tasks)
            .apply {
                layoutManager = LinearLayoutManager(this@Incomplete)
                adapter = mAdapter
            }
    }


}
