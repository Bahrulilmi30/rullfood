package com.catnip.rullfood.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.catnip.rullfood.R
import com.catnip.rullfood.databinding.FragmentHomeBinding
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.presentation.details.DetailActivity
import com.catnip.rullfood.presentation.home.adapter.AdapterLayoutMode
import com.catnip.rullfood.presentation.home.adapter.CategoryListAdapter
import com.catnip.rullfood.presentation.home.adapter.FoodListAdapter
import com.catnip.rullfood.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    //    private val viewModel: HomeViewModel by viewModel<HomeViewModel>()
    private val viewModel: HomeViewModel by viewModel()

    private val adapter: FoodListAdapter by lazy {
        FoodListAdapter(
            adapterLayoutMode = AdapterLayoutMode.GRID,
            onItemClick = {
                navigateToActivityDetail(it)
            }
        )
    }
    private val categoryAdapter: CategoryListAdapter by lazy {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        invokeData()
        observeData()
        setUpCategoryRecyclerView()
        setUpSwitch()
    }

    private fun setUpSwitch() {
        binding.inclMainMenu.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            (binding.inclMainMenu.rvMenu.layoutManager as GridLayoutManager).spanCount =
                if (isChecked) 1 else 2
            adapter.adapterLayoutMode =
                if (isChecked) AdapterLayoutMode.LINEAR else AdapterLayoutMode.GRID
            adapter.refreshList()
        }
    }

    private fun setUpCategoryRecyclerView() {
        binding.inclTopMenu.rvCategory.adapter = categoryAdapter
    }

    private fun setUpRecyclerView() {
        val span = if (adapter.adapterLayoutMode == AdapterLayoutMode.GRID) 2 else 1
        binding.inclMainMenu.rvMenu.adapter = adapter
        binding.inclMainMenu.rvMenu.layoutManager = GridLayoutManager(requireContext(), span)
    }

    private fun observeData() {
        observeUser()
        observeMenu()
    }

    private fun observeUser() {
        viewModel.userProfile.observe(viewLifecycleOwner) {
            val fullName = it?.fullName ?: "User"
            binding.InclHeader.tvHelloUser.text = getString(R.string.hello, fullName)
        }
    }

    private fun observeMenu() {
        viewModel.menu.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.inclMainMenu.rvMenu.isVisible = true
                    binding.inclMainMenu.layoutStateMenu.root.isVisible = false
                    binding.inclMainMenu.layoutStateMenu.tvError.isVisible = false
                    binding.inclMainMenu.layoutStateMenu.pbLoading.isVisible = false
                    it.payload?.let { menus ->
                        adapter.setData(menus)
                    }
                },
                doOnLoading = {
                    binding.inclMainMenu.layoutStateMenu.root.isVisible = true
                    binding.inclMainMenu.layoutStateMenu.pbLoading.isVisible = true
                    binding.inclMainMenu.layoutStateMenu.tvError.isVisible = false
                    binding.inclMainMenu.rvMenu.isVisible = false
                },
                doOnError = {
                    binding.inclMainMenu.rvMenu.isVisible = false
                    binding.inclTopMenu.layoutStateCategory.tvError.error =
                        it.exception?.message.toString()
                    Toast.makeText(
                        requireContext(),
                        it.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
        viewModel.category.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.inclTopMenu.rvCategory.isVisible = true
                    binding.inclTopMenu.layoutStateCategory.root.isVisible = false
                    binding.inclTopMenu.layoutStateCategory.tvError.isVisible = false
                    binding.inclTopMenu.layoutStateCategory.pbLoading.isVisible = false
                    it.payload?.let { category ->
                        categoryAdapter.setData(category)
                    }
                },
                doOnLoading = {
                    binding.inclTopMenu.layoutStateCategory.root.isVisible = true
                    binding.inclTopMenu.rvCategory.isVisible = false
                    binding.inclTopMenu.layoutStateCategory.pbLoading.isVisible = true
                    binding.inclTopMenu.layoutStateCategory.tvError.isVisible = false
                },
                doOnError = {
                    binding.inclTopMenu.rvCategory.isVisible = false
                    binding.inclTopMenu.layoutStateCategory.tvError.isVisible = true
                    binding.inclTopMenu.layoutStateCategory.tvError.error =
                        it.exception?.message.toString()
                    Toast.makeText(
                        requireContext(),
                        it.exception?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun invokeData() {
        viewModel.getCurrentUser()
        viewModel.getMenus()
        viewModel.getCategory()
    }
}
