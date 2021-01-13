package com.alxd.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alxd.todoapp.R
import com.alxd.todoapp.data.viewmodel.ToDoViewModel
import com.alxd.todoapp.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, { data->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner,{
            showEmptyDatabaseViews(it)
        })

        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete_all){
            confirmRemovalAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemovalAll(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Si"){_,_ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Se ha removido todo!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Eliminar todo?")
        builder.setMessage("Estas seguro que quieres remover todo?")
        builder.create().show()
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean){
        if(emptyDatabase){
            view?.ivNoData?.visibility = View.VISIBLE
            view?.tvNoData?.visibility = View.VISIBLE
        }else{
            view?.ivNoData?.visibility = View.INVISIBLE
            view?.tvNoData?.visibility = View.INVISIBLE
        }
    }

}