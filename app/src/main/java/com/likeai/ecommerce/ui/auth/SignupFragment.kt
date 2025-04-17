package com.likeai.ecommerce.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.likeai.ecommerce.R
import com.likeai.ecommerce.databinding.FragmentSignupBinding
import com.likeai.ecommerce.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private val viewModel: SignupViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        with(binding) {
            btnSignup.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if (validateInput(name, email, password, confirmPassword)) {
                    viewModel.signup(name, email, password)
                }
            }

            btnLogin.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnSignup.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            showError(error)
        }

        viewModel.signupSuccess.observe(viewLifecycleOwner) {
            showMessage(getString(R.string.msg_signup_success))
            findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty()) {
            showError("Please enter your name")
            return false
        }
        if (email.isEmpty()) {
            showError("Please enter your email")
            return false
        }
        if (password.isEmpty()) {
            showError("Please enter your password")
            return false
        }
        if (password.length < 6) {
            showError("Password must be at least 6 characters")
            return false
        }
        if (password != confirmPassword) {
            showError("Passwords do not match")
            return false
        }
        return true
    }
} 