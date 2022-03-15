package com.rachel.noteapp.fragments
//buat yg butten tambah itu yg dari home kl di pencet bisa pindah ke addFragment
import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rachel.noteapp.R
import com.rachel.noteapp.data.model.NoteData
import com.rachel.noteapp.data.model.Priority
import com.rachel.noteapp.fragments.list.ListFragmentDirections

class BindingAdapters {

    companion object{
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate : Boolean ){
            view.setOnClickListener{
                if (navigate){
                    view.findNavController().navigate(R.id.action_addFragment_to_listFragment)
                }
            }
        }
//buat ngecek database kita kosong apa gk
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>){
            when(emptyDatabase.value){
                //kalo database kosong maka boxnya akan di tampilkan nah view ini adalah data
                true -> view.visibility = View.VISIBLE
                //kalo ada maka di munculin boxnya
                false -> view.visibility = View.INVISIBLE
            }
        }
        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority){
            when(priority){
                Priority.HIGH -> {view.setSelection(0)}
                Priority.MEDIUM -> {view.setSelection(1)}
                Priority.LOW -> {view.setSelection(2)}
            }
        }

//ini itu buat fungsi buat indikator buat kl dia ngebuat data baru terus prioritynya dia memilih high,medium/low maka akan berubah warna nya
        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView, priority: Priority){
            when(priority){
                Priority.HIGH -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}
                Priority.MEDIUM -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))}
                Priority.LOW -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
            }
        }
//bikin fungsi buat update data (ngubah data)
        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentItem: NoteData){
            view.setOnClickListener {
                //bakal pindah dr listfra ke updtfra
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment2(currentItem)
                view.findNavController().navigate(action)
            }
        }
    }
}