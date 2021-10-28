package com.lab2.notes

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lab2.notes.db.NotesDatabase
import com.lab2.notes.db.Note
import com.lab2.notes.db.NoteRepository
import com.lab2.notes.viewModels.NoteViewModel
import com.lab2.notes.viewModels.NoteViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class HomeFragment : Fragment() {
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(NoteRepository(NotesDatabase.getDatabase(requireContext()).noteDao()))
    }

    var adapter: NotesAdapter = NotesAdapter()
    val notes: LiveData<List<Note>> by lazy { noteViewModel.allNotes }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment).addToBackStack(fragment.javaClass.simpleName)
            commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.setHasFixedSize(true)

        recycler_view.layoutManager =
            StaggeredGridLayoutManager(
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> 2
                    Configuration.ORIENTATION_LANDSCAPE -> 3
                    else -> 2
                }
                , StaggeredGridLayoutManager.VERTICAL)


            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        notes.observe(viewLifecycleOwner, Observer {
            adapter.setNotes(notes.value as ArrayList<Note>)
            recycler_view.adapter = adapter
        })

        adapter.setListener(listener)

        createNoteBtn.setOnClickListener {
            replaceFragment(NoteFragment.newInstance())
        }

        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var foundNotes = ArrayList<Note>()
                if (notes.value != null && p0 != null) {

                    for (note in notes.value!!) {
                        if (note.title.lowercase(Locale.getDefault()).contains(p0.toString())) {
                            foundNotes.add(note)
                        }
                    }

                }

                adapter.setNotes(foundNotes)
                adapter.notifyDataSetChanged()
                return true
            }
        })
    }

    private val listener = object : NotesAdapter.OnItemClickListener {

        override fun onClicked(noteId: Int?) {

            if (noteId != null) {
                var note: Note? = null
                for (n in notes.value!!) {
                    if (n.id == noteId) {
                        note = n
                    }
                }
                if (note != null) {
                    val bundle = Bundle()
                    bundle.putParcelable("note", note)
                    bundle.putInt("noteId", noteId)

                    val fragment = NoteFragment.newInstance()
                    fragment.arguments = bundle

                    replaceFragment(fragment)
                }
            }
        }
    }
}
