package com.rachel.noteapp.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rachel.noteapp.R
import com.rachel.noteapp.data.model.NoteData
import com.rachel.noteapp.data.model.Priority

class SharedViewModels(application: Application): AndroidViewModel(application) {

    //kita bikin var yg dia bakalan ngecek apakah databasenya itu kosong apa gk
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    //nah ini kita bikin fungsi yang buat ngecek apakah value kosong apa gk
    fun checkIfDatabaseEmpty(noteData: List<NoteData>){
        emptyDatabase.value = noteData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?,view: View?, position: Int, id: Long) {
            when(position){
                0 -> {
                    (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))
                }
                1 -> {
                    (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))
                }
                2 -> {
                    (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))
                }

            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
    fun verifyDataFromUser(title: String, desc: String): Boolean{
        return if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)){
            false
        }else !(title.isEmpty() || desc.isEmpty())
    }

    fun parsePriority(priority: String): Priority{
        return when (priority){
            "High Priority" -> {
                Priority.HIGH
            }
            "Medium Priority" -> {
                Priority.MEDIUM
            }
            "Low Priority" -> {
                Priority.LOW
            }
            else -> {
                Priority.LOW
            }
        }

    }
}