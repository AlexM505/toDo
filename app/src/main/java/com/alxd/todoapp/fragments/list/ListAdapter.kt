package com.alxd.todoapp.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alxd.todoapp.R
import com.alxd.todoapp.data.models.Priority
import com.alxd.todoapp.data.models.ToDoData
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tvTitle.text = dataList[position].title
        holder.itemView.tvDesc.text = dataList[position].description

        when(dataList[position].priority){
            Priority.HIGH -> holder.itemView.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.red
            ))
            Priority.MEDIUM -> holder.itemView.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.yellow
            ))
            Priority.LOW -> holder.itemView.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.green
            ))
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>){
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}