package com.alxd.todoapp.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alxd.todoapp.R
import com.alxd.todoapp.data.models.Priority
import com.alxd.todoapp.data.models.ToDoData
import com.alxd.todoapp.data.viewmodel.ToDoViewModel
import com.alxd.todoapp.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true)

        view.etCurrentTitle.setText(args.currentItem.title)
        view.etCurrentDescription.setText(args.currentItem.description)
        view.spinnerCurrentPriorities.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        view.spinnerCurrentPriorities.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_save){
            updateItem()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateItem(){
        val title = etCurrentTitle.text.toString()
        val desc = etCurrentDescription.text.toString()
        val getPriority = spinnerCurrentPriorities.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title, desc)
        if(validation){
            //Actualizando a base de datos
            val updatedItem = ToDoData(
                    args.currentItem.id,
                    title,
                    mSharedViewModel.parsePriority(getPriority),
                    desc
            )

            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(),"Se ha actualizado correctamente!",Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Por favor ingrese texto en todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

}