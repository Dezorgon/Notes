package com.lab2.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lab2.notes.db.Note
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var notes :ArrayList<Note> = ArrayList()

    fun setNotes(notes: ArrayList<Note>){
        this.notes = notes
    }

    private var listener: OnItemClickListener? = null

    fun setListener(listener: OnItemClickListener?){
        this.listener = listener
    }

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)

        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        if (notes[position].title == ""){
            holder.itemView.cardTitle.isVisible = false
        }

        holder.itemView.cardTitle.text = notes[position].title
        holder.itemView.cardText.text = notes[position].text
        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(notes[position].id)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    interface OnItemClickListener{
        fun onClicked(noteId: Int?)
    }
}