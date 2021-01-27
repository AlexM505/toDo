package com.alxd.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.alxd.todoapp.R
import com.alxd.todoapp.data.models.ToDoData
import com.alxd.todoapp.data.viewmodel.ToDoViewModel
import com.alxd.todoapp.databinding.FragmentListBinding
import com.alxd.todoapp.fragments.SharedViewModel
import com.alxd.todoapp.fragments.list.adapter.ListAdapter
import com.alxd.todoapp.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

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

        //cerrar teclado abiertos de los fragments
        hideKeyboard(requireActivity())

        return binding.root //view
    }

    private fun setupRecyclerview() {
        val recyclerView = binding.recyclerView // view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //Animacion
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                //Toast.makeText(requireContext(), "Se ha eliminado: '${itemToDelete.title}'", Toast.LENGTH_SHORT).show()

                //Restaurar el item eliminado
                restoreDeletedData(viewHolder.itemView, itemToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData){
        val snackbar = Snackbar.make(view, "'${deletedItem.title}' eliminado!",Snackbar.LENGTH_LONG)
        snackbar.setAction("Deshacer"){
            mToDoViewModel.insertData(deletedItem)
            //adapter.notifyDataSetChanged()
        }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_delete_all -> confirmRemovalAll()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list->
            list?.let {
                adapter.setData(it)
            }
        })
    }

}