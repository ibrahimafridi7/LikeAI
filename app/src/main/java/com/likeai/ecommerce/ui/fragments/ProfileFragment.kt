package com.likeai.ecommerce.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.likeai.ecommerce.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        auth.currentUser?.let { user ->
            binding.userName.text = user.displayName ?: "User"
            binding.userEmail.text = user.email
        }
    }

    private fun setupClickListeners() {
        binding.myOrdersButton.setOnClickListener {
            // TODO: Navigate to orders
            showFeatureNotAvailable()
        }

        binding.shippingAddressButton.setOnClickListener {
            // TODO: Navigate to shipping address
            showFeatureNotAvailable()
        }

        binding.paymentMethodsButton.setOnClickListener {
            // TODO: Navigate to payment methods
            showFeatureNotAvailable()
        }

        binding.settingsButton.setOnClickListener {
            // TODO: Navigate to settings
            showFeatureNotAvailable()
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            )
        }
    }

    private fun showFeatureNotAvailable() {
        Toast.makeText(requireContext(), "Feature coming soon!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 