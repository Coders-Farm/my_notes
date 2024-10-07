package com.example.mynotes.ui

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentCreateNotesBinding
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.ui.adapter.NotesAdapter
import com.example.mynotes.utils.Constants
import com.example.mynotes.utils.NetworkResponse
import com.example.mynotes.viewmodels.NotesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding: FragmentHomeBinding
        get() = _binding!!

    private lateinit var adapter:NotesAdapter

    private val notesListViewModel by activityViewModels<NotesListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //navigate
        navigateToNext()

        getAllNotes()

        getAllNotesResponse()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNext(){
        binding.btnAddNote.setOnClickListener {
            findNavController()
                .navigate(R.id.action_homeFragment_to_createNotesFragment,)
        }
    }

    private fun setUpRecyclerView(noteList: List<NotesResponse>){
        adapter = NotesAdapter(noteList)
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)
        binding.rvNotes.adapter = adapter

        adapter.callBack={ isEdit , response->

            val bundle = bundleOf(
                Pair(Constants.BundleKeys.IS_EDIT,isEdit),
                Pair(Constants.BundleKeys.NOTES_RESPONSE,response),
                )

            findNavController().navigate(R.id.action_homeFragment_to_createNotesFragment,bundle)
        }
    }

    private fun getAllNotes(){
        notesListViewModel.getAllNotes()
    }

    private fun getAllNotesResponse()= with(binding){

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                notesListViewModel
                    .stateFlow
                    .collect{ response ->
                        progressBar.isVisible = false
                        tvNoNotes.isVisible = false

                        response?.let {
                            when(it){
                                is NetworkResponse.Loading -> {
                                    progressBar.isVisible = true
                                }
                                is NetworkResponse.Success -> {
                                    if (it.data?.isEmpty() == true){
                                        tvNoNotes.isVisible = true
                                    }else{
                                        setUpRecyclerView(it.data as List<NotesResponse>)
                                    }
                                }
                                is NetworkResponse.Error -> {
                                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }

            }
        }
    }

}