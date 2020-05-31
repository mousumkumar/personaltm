package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Items(var item:String)

class MyItemAdapter(val items:List<Items>, context: Context, val listener : (Int) -> Unit):RecyclerView.Adapter<MyItemAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(items[position], position, listener)

    class MyViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemText = itemView.findViewById<TextView>(R.id.items)
        fun bind(item:Items, position: Int, listener: (Int) -> Unit) = with(itemView) {
            itemText.text = item.item
            itemView.setOnClickListener{
                listener(position)
            }
        }
    }

}