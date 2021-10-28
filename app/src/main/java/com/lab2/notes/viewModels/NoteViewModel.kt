package com.lab2.notes.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lab2.notes.db.Note
import com.lab2.notes.db.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel (private val repo: NoteRepository) : ViewModel() {
    val allNotes: LiveData<List<Note>> = repo.allNotes

    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repo.insertNote(note)
    }
    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repo.updateNote(note)
    }
    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repo.deleteNote(note)
    }
}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("")
    }
}