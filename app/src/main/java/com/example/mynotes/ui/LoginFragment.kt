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
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.databinding.FragmentLoginBinding
import com.example.mynotes.models.UserRequest
import com.example.mynotes.utils.Constants
import com.example.mynotes.utils.Extensions
import com.example.mynotes.utils.NetworkResponse
import com.example.mynotes.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding: FragmentLoginBinding
        get() = _binding!!

    private val loginViewModel by  activityViewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //navigate
        navigateToNext()

        loginResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNext(){
        binding.btnLogin.setOnClickListener {
            if (checkValidation()) {
                callLoginApi()
            }
        }

        binding.tvNoAccount.setOnClickListener {
            findNavController()
                .navigate(R.id.action_loginFragment_to_createAccountFragment)
        }
    }

    private fun callLoginApi()= with(binding){
        val request = UserRequest(
            email = edEmail.text.toString(),
            password = edPassword.text.toString()
        )

        loginViewModel.login(userRequest = request)
    }

    private fun loginResponse()= with(binding){

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                loginViewModel
                    .stateFlow
                    .collect{ response->
                        progressBar.isVisible = false
                        response?.let {
                            when(it){
                                is NetworkResponse.Loading ->{
                                    progressBar.isVisible = true
                                }
                                is NetworkResponse.Success ->{
                                    findNavController()
                                        .navigate(R.id.action_loginFragment_to_homeFragment,)
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

    private fun checkValidation(): Boolean {
        with(binding) {
            if (edEmail.text?.isEmpty() == true) {
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