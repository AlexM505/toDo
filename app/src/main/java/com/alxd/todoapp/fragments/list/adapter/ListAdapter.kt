package com.alxd.todoapp.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alxd.todoapp.data.models.ToDoData
import com.alxd.todoapp.databinding.RowLayoutBinding

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

//    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}
    class MyViewHolder(private val binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
//        return MyViewHolder(view)
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = dataList[position]
        holder.bind(currentItem)
//        holder.itemView.tvTitle.text = dataList[position].title
//        holder.itemView.tvDesc.text = dataList[position].description
//        holder.itemView.rowBackground.setOnClickListener {
//            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
//            holder.itemView.findNavController().navigate(action)
//        }
//
//        when(dataList[position].priority){
//            Priority.HIGH -> holder.itemView.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
//                    holder.itemView.context,
//                    R.color.red
//            ))
//            Priority.MEDIUM -> holder.itemView.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
//                    holder.itemView.context,
//                    R.color.yellow
//            ))
//            Priority.LOW -> holder.itemView.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(
//                    holder.itemView.context,
//                    R.color.green
//            ))
//        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>){
        //Implementando DiffUil
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
//        notifyDataSetChanged()
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}