package com.catnip.rullfood.presentation.changepforile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catnip.rullfood.databinding.ActivityProfile1Binding
import com.catnip.rullfood.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity1 : AppCompatActivity() {
    private val binding: ActivityProfile1Binding by lazy {
        ActivityProfile1Binding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel1 by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpUser()
        invokeData()
        setClickListener()
        observeResult()
    }

    private fun observeResult() {
        viewModel.updateResultProfile.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Update Profile Success", Toast.LENGTH_LONG).show()
                    finish()
                }

            )
        }
    }

    private fun setClickListener() {
        binding.btnSave.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val fullName = binding.etUsername.text.toString()
        viewModel.updateProfile(fullName)
    }

    private fun invokeData() {
        viewModel.getCurrentUser()
    }

    private fun setUpUser() {
        viewModel.userProfile.observe(this) {
            binding.etUsername.setText(it?.fullName)
            Toast.makeText(this, it?.fullName, Toast.LENGTH_SHORT).show()
        }
    }
}
