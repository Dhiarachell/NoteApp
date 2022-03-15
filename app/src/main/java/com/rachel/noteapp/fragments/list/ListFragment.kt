package com.rachel.noteapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rachel.noteapp.R
import com.rachel.noteapp.data.model.NoteData
import com.rachel.noteapp.data.viewModelData.NoteViewModel
import com.rachel.noteapp.databinding.FragmentListBinding
import com.rachel.noteapp.fragments.SharedViewModels
import com.rachel.noteapp.fragments.adapter.ListAdapter
import com.rachel.noteapp.utils.hideKeyboard
import jp.wasabeef.recyclerview.animators.LandingAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener{

    private val mNotesViewModel : NoteViewModel by viewModels()
    private val adapter : ListAdapter by lazy { ListAdapter() }
    private val mSharedViewModels : SharedViewModels by viewModels()
    private var _listBinding : FragmentListBinding? = null
    private val listBinding get() = _listBinding!!

       override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {
           // Inflate the layout for this fragment
           _listBinding = FragmentListBinding.inflate(inflater, container, false)
           listBinding.lifecycleOwner = this
           listBinding.mSharedViewModel = mSharedViewModels

           setUpRecyclerView()

           mNotesViewModel.getAllData.observe(viewLifecycleOwner,{ data ->
               mSharedViewModels.checkIfDatabaseEmpty(data)
               adapter.setData(data)
           })

           setHasOptionsMenu(true)
           hideKeyboard(requireActivity())
           return listBinding.root


       }

    private fun setUpRecyclerView() {listBinding.rvList.adapter = adapter
        listBinding.rvList.layoutManager = StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL)
        listBinding.rvList.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }

        swipeToDelete(listBinding.rvList)

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, directions: Int){
                val deleteItem = adapter.dataList[viewHolder.adapterPosition]
                mNotesViewModel.deleteData(deleteItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeleteDta(viewHolder.itemView,deleteItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun restoreDeleteDta(view: View, deleteItem: NoteData) {
        val snackbar = Snackbar.make(
            view, "Deleted'${deleteItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo"){
            mNotesViewModel.insertData(deleteItem)
        }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> confirmRemoveAll()
            R.id.menu_priority_high -> mNotesViewModel.sortedByHighPriority.observe(this, {
                adapter.setData(it)
            })
            R.id.menu_priority_low -> mNotesViewModel.sortedByHighPriority.observe(this, {
                adapter.setData(it)
            })

        }
        return super.onOptionsItemSelected(item)
        }

    private fun confirmRemoveAll() {
        AlertDialog.Builder(requireContext())
            .setTitle("Dlete All?")
            .setMessage("Are You Sure Remove All?")
            .setPositiveButton("Yes"){_, _ ->
                mNotesViewModel.deleteAllData()
                Toast.makeText(requireContext(), "Successfully Removed All",
                Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("No",null)
            .create()
            .show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        mNotesViewModel.searchDatabase(searchQuery).observe(
            this, {
                list -> list.let { adapter.setData(it)}
            }
        )

    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onDestroy() {
        _listBinding = null
        super.onDestroy()
    }


}