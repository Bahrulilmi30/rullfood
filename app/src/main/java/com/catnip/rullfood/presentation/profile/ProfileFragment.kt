package com.catnip.rullfood.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.catnip.rullfood.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.catnip.rullfood.data.repository.UserRepository
import com.catnip.rullfood.data.repository.UserRepositoryImpl
import com.catnip.rullfood.databinding.FragmentProfileBinding
import com.catnip.rullfood.presentation.login.LoginViewModel
import com.catnip.rullfood.utils.GenericViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private  val viewModel : ProfileViewModel by viewModels {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)

        GenericViewModelFactory.create(ProfileViewModel(repo))
    }
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
    }

    private fun invokeData() {
        viewModel.getCurrentUser()
    }

    private fun setUpUser() {
        viewModel.userProfile.observe(viewLifecycleOwner){
            binding.etUsername.setText(it?.fullName)
            Toast.makeText(requireContext(), it?.fullName, Toast.LENGTH_SHORT).show()
        }
    }

}
