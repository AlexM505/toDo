package com.alxd.todoapp.fragments.update

import android.app.AlertDialog
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
import com.alxd.todoapp.databinding.FragmentUpdateBinding
import com.alxd.todoapp.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val view = inflater.inflate(R.layout.fragment_update, container, false)
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args

        setHasOptionsMenu(true)

//        view.etCurrentTitle.setText(args.currentItem.title)
//        view.etCurrentDescription.setText(args.currentItem.description)
//        view.spinnerCurrentPriorities.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding.spinnerCurrentPriorities.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
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

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Si"){_,_ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(), "'${args.currentItem.title}' Ha sido removido satisfactoriamente!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Eliminar '${args.currentItem.title}'")
        builder.setMessage("Estas seguro que quieres remover '${args.currentItem.title}'?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}