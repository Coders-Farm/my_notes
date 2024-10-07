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
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentCreateAccountBinding
import com.example.mynotes.models.UserRequest
import com.example.mynotes.utils.Constants
import com.example.mynotes.utils.Extensions
import com.example.mynotes.utils.MyNotesSharedPref
import com.example.mynotes.utils.NetworkResponse
import com.example.mynotes.viewmodels.CreateAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null

    private val binding: FragmentCreateAccountBinding
        get() = _binding!!

    private val createAccountViewModel by activityViewModels<CreateAccountViewModel>()

    @Inject
    lateinit var myNotesSharedPref: MyNotesSharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //if already login then move to home
        val token =myNotesSharedPref.getToken()
        if (!token.isNullOrEmpty()){
            findNavController().navigate(R.id.action_createAccountFragment_to_homeFragment)
        }

        //navigate
        navigateToNext()

        createAccountResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNext() {
        binding.btnCreate.setOnClickListener {
            if (checkValidation()) {
                callCreateAccountApi()
            }
        }

        binding.tvHaveAccount.setOnClickListener {
            findNavController()
                .navigate(R.id.action_createAccountFragment_to_loginFragment)
        }
    }

    private fun callCreateAccountApi() = with(binding) {

        val request = UserRequest(
            email = edEmail.text.toString(),
            password = edPassword.text.toString()
        )
        createAccountViewModel.createAccount(request)
    }

    private fun createAccountResponse() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createAccountViewModel
                    .stateFlow
                    .collect { response ->
                        progressBar.isVisible = false

                        response?.let {
                            when (it) {
                                is NetworkResponse.Loading -> {
                                    progressBar.isVisible = true
                                }

                                is NetworkResponse.Success -> {
                                    findNavController()
                                        .navigate(R.id.action_createAccountFragment_to_homeFragment)
                                }

                                is NetworkResponse.Error -> {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                        .show()
                                }

                            }
                        }

                    }
            }
        }
    }

    private fun checkValidation(): Boolean {
        with(binding) {
            if (edFullName.text?.isEmpty() == true) {
                Extensions.showMessage(requireContext(),
                    getString(R.string.please_enter_your_full_name))
                return false
            } else if (edEmail.text?.isEmpty() == true) {
                Extensions.showMessage(requireContext(), getString(R.string.please_enter_your_email))
                return false
            } else if (edPassword.text?.isEmpty() == true) {
                Extensions.showMessage(requireContext(),
                    getString(R.string.please_enter_your_password))
                return false
            } else if (edEmail.text?.toString()
                    ?.matches(Regex(Constants.ValidationMatchers.EMAIL)) != true
            ) {
                Extensions.showMessage(requireContext(),
                    getString(R.string.please_enter_valid_email))
                return false
            } else if ((edPassword.text?.toString()?.length ?: 0) < 8) {
                Extensions.showMessage(requireContext(),
                    getString(R.string.password_should_be_8_charters_or_more))
                return false
            } else {
                return true
            }
        }
    }


}