package com.example.myapplication

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.assigned_dialog.*
import kotlinx.android.synthetic.main.fragment_first.view.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var mAdapter: FirebaseRecyclerAdapter< Tasks, TaskViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.fab2.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val uid = FirebaseAuth.getInstance().uid

        val query = FirebaseDatabase.getInstance().reference.child(uid!!).child("assigned")

        val options = FirebaseRecyclerOptions.Builder<Tasks> ()
            .setQuery(query, Tasks::class.java)
            .setLifecycleOwner(activity)
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
                    val dialog = Dialog(context!!)
                    dialog.setContentView(R.layout.assigned_dialog)
                    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    dialog.dialog_title.text = model.title
                    dialog.dialog_description.text = model.description
                    dialog.move_to_completed.setOnClickListener {
                        FirebaseDatabase.getInstance().reference.child(uid!!).child("completed").push()
                            .setValue(model)
                            .addOnCompleteListener {
                                FirebaseDatabase.getInstance().reference.child(uid).child("assigned").child(child!!)
                                    .removeValue()
                            }
                        dialog.dismiss()
                    }
                    dialog.move_to_incomplete.setOnClickListener {
                        FirebaseDatabase.getInstance().reference.child(uid!!).child("incomplete").push()
                            .setValue(model)
                            .addOnCompleteListener {
                                FirebaseDatabase.getInstance().reference.child(uid).child("assigned").child(child!!)
                                    .removeValue()
                            }
                        dialog.dismiss()
                    }
                    dialog.delete.setOnClickListener {
                        FirebaseDatabase.getInstance().reference.child(uid).child("assigned").child(child!!)
                            .removeValue()
                        dialog.dismiss()
                    }
                    dialog.show()
                })
            }

        }
        

        val recyclerView = view.findViewById<RecyclerView>(R.id.tasks)
            .apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = mAdapter
            }
    }
}
