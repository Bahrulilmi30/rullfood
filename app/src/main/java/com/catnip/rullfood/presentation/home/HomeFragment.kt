package com.catnip.rullfood.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSource
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSourceImpl
import com.catnip.rullfood.data.network.api.service.RestaurantService
import com.catnip.rullfood.data.repository.MenuRepositoryImpl
import com.catnip.rullfood.databinding.FragmentHomeBinding
import com.catnip.rullfood.utils.GenericViewModelFactory
import com.catnip.rullfood.utils.proceedWhen

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel : HomeViewModel by viewModels {
        val service = RestaurantService.invoke()
        val dataSource = RestaurantDataSourceImpl(service)
        val repository = MenuRepositoryImpl(dataSource)
        GenericViewModelFactory.create(HomeViewModel(repository))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        invokeData()
        observeData()
    }

    private fun observeData() {
        viewModel.menu.observe( viewLifecycleOwner){
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let { menus ->
                        binding.textHome.text = menus.toString()
                    }
                }
            )
        }
    }

    private fun invokeData() {
       viewModel.getMenus()
    }

}

