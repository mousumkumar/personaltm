package com.example.myapplication

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main2.view.*

class Main2Activity : AppCompatActivity() {


    lateinit var recyclerView:RecyclerView

    val listItems = listOf<Items>(
        Items("Assigned Tasks"),
        Items("Completed Tasks"),
        Items("Incomplete Tasks")
    )

    val context = this



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        recyclerView = findViewById(R.id.main_menu)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemAdapter(listItems, context){
                when(it) {
                    0 -> intent = Intent(context,Assigned::class.java)
                    1 -> intent = Intent(context,Completed::class.java)
                    2 -> intent = Intent(context,Incomplete::class.java)
                }
                startActivity(intent)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_logout) {
            AuthUI.getInstance().signOut(this)
            finish()
        }

        if (item.itemId == R.id.action_about) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.about_dialog)
            dialog.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
