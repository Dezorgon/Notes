package com.lab2.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lab2.notes.db.NotesDatabase
import com.lab2.notes.db.Note
import com.lab2.notes.db.NoteRepository
import com.lab2.notes.viewModels.NoteViewModel
import com.lab2.notes.viewModels.NoteViewModelFactory
import kotlinx.android.synthetic.main.fragment_create_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteFragment : Fragment() {
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(NoteRepository(NotesDatabase.getDatabase(requireContext()).noteDao()))
    }

    private var noteId = -1
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = requireArguments().getInt("noteId", -1)
            note = requireArguments().getParcelable("note")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (noteId != -1 && note != null) {

            noteTitle.setText(note!!.title)
            noteDateTime.text = note!!.date_time
            noteText.setText(note!!.text)


            deleteNoteBtn.visibility = View.VISIBLE

            deleteNoteBtn.setOnClickListener {
                deleteNote()
            }
        }

        btnEnd.setOnClickListener {
            if (noteId == -1) {
                saveNote()
            }
            else{
                updateNote()
            }
        }


        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getDate(): String{
        val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm")
        return formatter.format(Date())
    }

    private fun saveNote() {
        val note = Note(
            noteTitle.text.toString(),
            getDate(),
            noteText.text.toString()
        )

        if (note.text != "" || note.title != ""){
            noteViewModel.insertNote(note)
        }

        btnBack.callOnClick()
    }

    private fun updateNote(){
        if (note!!.title != noteTitle.text.toString() ||
            note!!.text != noteText.text.toString()){

            note!!.title = noteTitle.text.toString()
            note!!.text = noteText.text.toString()

            note!!.date_time = getDate()

            if (note!!.text == "" && note!!.title == ""){
                noteViewModel.deleteNote(note!!)
            }
            else{
                noteViewModel.updateNote(note!!)
            }
        }

        btnBack.callOnClick()
    }

    private fun deleteNote(){
        noteViewModel.deleteNote(note!!)
        requireActivity().supportFragmentManager.popBackStack()
    }

}