package com.lab2.notes.db;

import androidx.lifecycle.LiveData;


class NoteRepository(private val noteDao : NoteDao) {
    val allNotes : LiveData<List<Note>> = noteDao.getAllNotes()

    fun insertNote(note: Note){
        noteDao.insertNote(note)
    }
    fun updateNote(note: Note){
        noteDao.updateNote(note)
    }
    fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }
    fun getNote(id: Int): Note {
        return noteDao.getNoteById(id)
    }

}