package com.alxd.todoapp.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.alxd.todoapp.R
import com.alxd.todoapp.data.models.Priority
import com.alxd.todoapp.data.models.ToDoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>){
        emptyDatabase.value = toDoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red)) }
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow)) }
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green)) }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}

    }

    fun verifyDataFromUser(title:String, desc:String): Boolean{
        return if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)){
            false
        }else !(title.isEmpty() || desc.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when(priority){
            "Prioridad Alta" -> {
                Priority.HIGH}
            "Prioridad Media" -> {
                Priority.MEDIUM}
            "Prioridad Baja" -> {
                Priority.LOW}
            else -> Priority.LOW
        }
    }

    fun parsePriorityToInt(priority: Priority): Int{
        return when(priority){
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

}