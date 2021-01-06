package com.alxd.todoapp.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alxd.todoapp.R
import com.alxd.todoapp.data.models.Priority
import com.alxd.todoapp.data.models.ToDoData
import com.alxd.todoapp.data.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = etTitle.text.toString()
        val mPriority =  spinnerPriorities.selectedItem.toString()
        val mDescription = etDescription.text.toString()

        val validation = verifyDataFromUser(mTitle, mDescription)
        if(validation){
            //Insertar a base de datos
            val newToDo = ToDoData(
                    0,
                    mTitle,
                    parsePriority(mPriority),
                    mDescription
            )

            mToDoViewModel.insertData(newToDo)
            Toast.makeText(requireContext(), "Se ha agregado con exito!!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Por favor ingrese texto en todos los campos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun verifyDataFromUser(title:String, desc:String): Boolean{
        return if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)){
            false
        }else !(title.isEmpty() || desc.isEmpty())
    }

    private fun parsePriority(priority: String):Priority{
        return when(priority){
            "Prioridad Alta" -> {Priority.HIGH}
            "Prioridad Media" -> {Priority.MEDIUM}
            "Prioridad Baja" -> {Priority.LOW}
            else -> Priority.LOW
        }
    }
}