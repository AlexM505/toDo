package com.alxd.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alxd.todoapp.R
import com.alxd.todoapp.data.viewmodel.ToDoViewModel
import com.alxd.todoapp.databinding.FragmentListBinding
import com.alxd.todoapp.fragments.SharedViewModel
import com.alxd.todoapp.fragments.list.adapter.ListAdapter

class ListFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view =  inflater.inflate(R.layout.fragment_list, container, false)
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        //Setup RecyclerView
        setupRecyclerview()

        //Observe LiveData
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, { data->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //TODO: Se agrego funcionalidad Databinding!!
//        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner,{
//            showEmptyDatabaseViews(it)
//        })

        //TODO: Se agrego funcionalidad Databinding!!
//        view.floatingActionButton.setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_addFragment)
//        }

        //Set Menu
        setHasOptionsMenu(true)

        return binding.root //view
    }

    private fun setupRecyclerview() {
        val recyclerView = binding.recyclerView // view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(itemToDelete)
                Toast.makeText(requireContext(), "Se ha eliminado: '${itemToDelete.title}'", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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

    //TODO: Se agrego funcionalidad Databinding!!
//    private fun showEmptyDatabaseViews(emptyDatabase: Boolean){
//        if(emptyDatabase){
//            view?.ivNoData?.visibility = View.VISIBLE
//            view?.tvNoData?.visibility = View.VISIBLE
//        }else{
//            view?.ivNoData?.visibility = View.INVISIBLE
//            view?.tvNoData?.visibility = View.INVISIBLE
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}