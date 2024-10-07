package com.example.mynotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentCreateNotesBinding
import com.example.mynotes.models.NotesResponse
import com.example.mynotes.utils.Constants
import com.example.mynotes.utils.Extensions
import com.example.mynotes.utils.NetworkResponse
import com.example.mynotes.viewmodels.AddNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class CreateNotesFragment : Fragment() {

    private var _binding: FragmentCreateNotesBinding? = null

    private val binding: FragmentCreateNotesBinding
        get() = _binding!!

    private val addNotesNoteViewModel by activityViewModels<AddNoteViewModel>()

    private var isEdit:Boolean = false
    private var notesResponse:NotesResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateNotesBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            isEdit = it.getBoolean(Constants.BundleKeys.IS_EDIT,false)
            notesResponse = it.getSerializable(Constants.BundleKeys.NOTES_RESPONSE) as? NotesResponse
        }

        setEditData()

        //navigate
        navigateToNext()

        createNoteResponse()

        updateNoteResponse()

        deleteNoteResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNext(){
        binding.btnAddNote.setOnClickListener {
            if (checkValidation()) {
                if (!isEdit) {
                    createNote()
                } else {
                    updateNote()
                }
            }
        }

        binding.btnDeleteNote.setOnClickListener {
            if (checkValidation()) {
                deleteNote()
            }
        }
    }

    private fun createNote()= with(binding){
        val request = NotesResponse(
            id =0,
            title = edTitle.text.toString(),
            description = edDescription.text.toString(),
            createdAt = Date()
        )
        addNotesNoteViewModel.createNote(request)
    }

    private fun createNoteResponse()= with(binding){

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                addNotesNoteViewModel.createNoteStateFlow
                    .collect{ response ->
                        progressBar.isVisible = false

                        response.let {
                            when(it){
                                is NetworkResponse.Loading ->{
                                    progressBar.isVisible = true
                                }
                                is NetworkResponse.Success ->{
                                    Toast.makeText(requireContext(),response.data,Toast.LENGTH_SHORT).show()

                                    findNavController()
                                        .navigate(R.id.action_createNotesFragment_to_homeFragment)
                                }
                                is NetworkResponse.Error ->{
                                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
            }
        }

    }

    private fun updateNote()= with(binding){
        val request = NotesResponse(
            id = notesResponse?.id?:0,
            title = edTitle.text.toString(),
            description = edDescription.text.toString(),
            createdAt = notesResponse?.createdAt?:Date()
        )
        addNotesNoteViewModel.updateNote(request)
    }

    private fun updateNoteResponse()= with(binding){

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                addNotesNoteViewModel.updateNoteStateFlow
                    .collect{ response ->
                        progressBar.isVisible = false

                        response.let {
                            when(it){
                                is NetworkResponse.Loading ->{
                                    progressBar.isVisible = true
                                }
                                is NetworkResponse.Success ->{
                                    Toast.makeText(requireContext(),response.data,Toast.LENGTH_SHORT).show()

                                    findNavController()
                                        .navigate(R.id.action_createNotesFragment_to_homeFragment)
                                }
                                is NetworkResponse.Error ->{
                                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
            }
        }

    }

    private fun deleteNote(){
        addNotesNoteViewModel.deleteNote(notesResponse as NotesResponse)
    }

    private fun deleteNoteResponse()= with(binding){

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                addNotesNoteViewModel.deleteNoteStateFlow
                    .collect{ response ->
                        progressBar.isVisible = false

                        response.let {
                            when(it){
                                is NetworkResponse.Loading ->{
                                    progressBar.isVisible = true
                                }
                                is NetworkResponse.Success ->{
                                    Toast.makeText(requireContext(),response.data,Toast.LENGTH_SHORT).show()

                                    findNavController()
                                        .navigate(R.id.action_createNotesFragment_to_homeFragment)
                                }
                                is NetworkResponse.Error ->{
                                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
            }
        }

    }

    private fun setEditData()= with(binding){
        if (isEdit){
            edTitle.setText(notesResponse?.title)
            edDescription.setText( notesResponse?.description)
            btnAddNote.setText(R.string.edit_note)
            tvTitle.setText(R.string.edit_note)
            btnDeleteNote.isVisible = true
        }
    }

    private fun checkValidation():Boolean{
        with(binding){
            if(edTitle.text?.isEmpty() == true){
                Extensions.showMessage(requireContext(), getString(R.string.please_enter_the_title))
                return false
            }else if (edDescription.text?.isEmpty() == true){
                Extensions.showMessage(requireContext(),
                    getString(R.string.please_enter_the_description))
                return false
            }else{
                return true
            }
        }
    }

}