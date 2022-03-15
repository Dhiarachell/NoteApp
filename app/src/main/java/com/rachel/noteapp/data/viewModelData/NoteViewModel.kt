package com.rachel.noteapp.data.viewModelData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rachel.noteapp.data.model.NoteData
import com.rachel.noteapp.data.model.NoteDatabase
import com.rachel.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application){
    private val noteDao = NoteDatabase.getDtabase(application).noteDao()
    private val repository : NoteRepository
    val getAllData : LiveData<List<NoteData>>
    val sortedByHighPriority : LiveData<List<NoteData>>
    val sortedByLowPriority : LiveData<List<NoteData>>

    init {
        repository = NoteRepository(noteDao)
        getAllData = repository.getAllData
        sortedByHighPriority = repository.sortByHighPriority
        sortedByLowPriority = repository.sortByLowPriority
    }

    fun insertData(noteData: NoteData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(noteData)
        }
    }

    fun updateData(noteData: NoteData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(noteData)
        }
    }

    fun deleteData(noteData: NoteData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(noteData)
        }
    }

    fun deleteAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }
    fun searchDatabase(searchQuery: String) : LiveData<List<NoteData>> {
        return repository.searchData(searchQuery)
    }
}