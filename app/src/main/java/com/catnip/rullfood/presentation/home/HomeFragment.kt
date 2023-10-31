package com.catnip.rullfood.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSource
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSourceImpl
import com.catnip.rullfood.data.network.api.service.RestaurantService
import com.catnip.rullfood.data.repository.MenuRepositoryImpl
import com.catnip.rullfood.databinding.FragmentHomeBinding
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.presentation.details.DetailActivity
import com.catnip.rullfood.presentation.home.adapter.AdapterLayoutMode
import com.catnip.rullfood.presentation.home.adapter.CategoryListAdapter
import com.catnip.rullfood.presentation.home.adapter.FoodListAdapter
import com.catnip.rullfood.utils.GenericViewModelFactory
import com.catnip.rullfood.utils.proceedWhen

class HomeFragment : Fragment() {

    private val adapter : FoodListAdapter by lazy{
        FoodListAdapter(
            adapterLayoutMode = AdapterLayoutMode.GRID,
            onItemClick = {
                navigateToActivityDetail(it)
            }
        )
    }
    private val categoryAdapter : CategoryListAdapter by lazy {
        CategoryListAdapter(
            onItemClick = {
                viewModel.getMenus(it.slug?.lowercase())
            }
        )
    }

    private fun navigateToActivityDetail(
        menu: Menu
    ) {
        DetailActivity.startActivity(requireContext(), menu)
    }

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
        setUpRecyclerView()
        invokeData()
        observeData()
        setUpCategoryRecyclerView()
    }

    private fun setUpCategoryRecyclerView() {
        binding.inclTopMenu.rvCategory.adapter = categoryAdapter
//        binding.inclTopMenu.rvCategory.layoutManager = (requireContext())
    }

    private fun setUpRecyclerView() {
        val span = if (adapter.adapterLayoutMode == AdapterLayoutMode.GRID) 2 else 1
        binding.inclMainMenu.rvMenu.adapter = adapter
        binding.inclMainMenu.rvMenu.layoutManager = GridLayoutManager(requireContext(), span)

    }

    private fun observeData() {
        viewModel.menu.observe( viewLifecycleOwner){
            it.proceedWhen(
                doOnSuccess = {
                    binding.inclMainMenu.rvMenu.isVisible = true
                    it.payload?.let { menus ->
                        adapter.setData(menus)
                    }
                },
                doOnLoading = {
                    binding.inclMainMenu.rvMenu.isVisible = false

                },
                doOnError = {
                    binding.inclMainMenu.rvMenu.isVisible = false
                }
            )
        }
        viewModel.category.observe(viewLifecycleOwner){
            it.proceedWhen (
                doOnSuccess = {
                    binding.inclTopMenu.rvCategory.isVisible = true
                    it.payload?.let { category ->
                        categoryAdapter.setData(category)
                    }
                },
                doOnLoading = {
                    binding.inclTopMenu.rvCategory.isVisible = false
                    binding.inclTopMenu.layoutStateCategory.pbLoading.isVisible = true
                    binding.inclTopMenu.layoutStateCategory.tvError.isVisible = false
                },
                doOnError = {
                    binding.inclTopMenu.rvCategory.isVisible = false
                    binding.inclTopMenu.layoutStateCategory.tvError.isVisible = true
                    binding.inclTopMenu.layoutStateCategory.tvError.error = it.exception?.message.toString()
                    Toast.makeText(requireContext(), it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun invokeData() {
       viewModel.getMenus()
        viewModel.getCategory()
    }

}

