package com.example.mynotes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.RecyclerNotesItemRowBinding
import com.example.mynotes.models.NotesResponse

class NotesAdapter(private val notesList: List<NotesResponse>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var callBack: ((Boolean, NotesResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerNotesItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = notesList[position]

        with(holder.binding) {
            tvTitle.text = data.title
            tvDescription.text = data.description
            layoutMain.setOnClickListener {
                callBack?.invoke(true, data)
            }

        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder(val binding: RecyclerNotesItemRowBinding) :
        RecyclerView.ViewHolder(binding.root)

}