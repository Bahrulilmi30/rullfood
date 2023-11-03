package com.catnip.rullfood.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.catnip.rullfood.R
import com.catnip.rullfood.data.database.AppDatabase
import com.catnip.rullfood.data.database.datasource.CartDataSource
import com.catnip.rullfood.data.database.datasource.CartDatabaseDataSource
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSourceImpl
import com.catnip.rullfood.data.network.api.service.RestaurantService
import com.catnip.rullfood.data.repository.CartRepository
import com.catnip.rullfood.data.repository.CartRepositoryImpl
import com.catnip.rullfood.databinding.FragmentCartBinding
import com.catnip.rullfood.model.Cart
import com.catnip.rullfood.presentation.cart.adapter.CartListAdapter
import com.catnip.rullfood.presentation.cart.adapter.CartListener
import com.catnip.rullfood.presentation.checkout.CheckoutActivity
import com.catnip.rullfood.utils.GenericViewModelFactory
import com.catnip.rullfood.utils.hideKeyboard
import com.catnip.rullfood.utils.proceedWhen
import com.catnip.rullfood.utils.toCurrencyFormat
import com.chuckerteam.chucker.api.ChuckerInterceptor

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

//    private val viewModel: CartViewModel by viewModel()

    private val viewModel: CartViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val cartdao = database.cartDao()
        val cartdataSource: CartDataSource = CartDatabaseDataSource(cartdao)
        val chuckerInterceptor = ChuckerInterceptor(requireContext().applicationContext)
        val service = RestaurantService.invoke(chuckerInterceptor)
        val apiDataSource = RestaurantDataSourceImpl(service)
        val repo: CartRepository = CartRepositoryImpl(cartdataSource, apiDataSource)
        GenericViewModelFactory.create(CartViewModel(repo))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener {
            override fun onMinusTotalItemCartClicked(cart: Cart) {
                viewModel.decreaseCart(cart)
            }

            override fun onPlusTotalItemCartClicked(cart: Cart) {
                viewModel.increaseCart(cart)
            }

            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.removeCart(cart)
            }

            override fun onUserDoneEditingNotes(cart: Cart) {
                viewModel.setCartNotes(cart)
                hideKeyboard()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpList()
        observeData()
        setClickListener()
    }

    private fun setUpList() {
        binding.rvCart.itemAnimator = null
        binding.rvCart.adapter = adapter
    }

    private fun observeData() {
        viewModel.cartList.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCart.isVisible = true
                    binding.cvSectionCheckout.isVisible = true

                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                    }
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.cvSectionCheckout.isVisible = false
                    binding.rvCart.isVisible = false
                },
                doOnError = { err ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvCart.isVisible = false
                },
                doOnEmpty = { data ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    binding.cvSectionCheckout.isVisible = false
                    data.payload?.let { (_, totalPrice) ->
                        binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                    }
                    binding.rvCart.isVisible = false
                }
            )
        }
    }

    private fun setClickListener() {
        binding.btnCheckout.setOnClickListener {
            context?.startActivity(Intent(requireContext(), CheckoutActivity::class.java))
        }
    }
}
