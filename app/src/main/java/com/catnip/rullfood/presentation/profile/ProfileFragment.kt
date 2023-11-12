package com.catnip.rullfood.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catnip.rullfood.databinding.FragmentProfileBinding
import com.catnip.rullfood.presentation.changepforile.ProfileActivity1
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUser()
        invokeData()
        setCLickListener()
    }

    override fun onResume() {
        super.onResume()
        invokeData()
    }

    private fun setCLickListener() {
        binding.ivEdit.setOnClickListener {
            navigateToEditProfile()
        }
    }

    private fun navigateToEditProfile() {
        val intent = Intent(requireContext(), ProfileActivity1::class.java)
        startActivity(intent)
    }

    private fun invokeData() {
        viewModel.getCurrentUser()
    }

    private fun setUpUser() {
        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.etUsername.setText(it?.fullName)
        }
    }
}
