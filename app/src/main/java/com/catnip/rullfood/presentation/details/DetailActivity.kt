package com.catnip.rullfood.presentation.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.catnip.rullfood.data.database.AppDatabase
import com.catnip.rullfood.data.database.datasource.CartDataSource
import com.catnip.rullfood.data.database.datasource.CartDatabaseDataSource
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSourceImpl
import com.catnip.rullfood.data.network.api.service.RestaurantService
import com.catnip.rullfood.data.repository.CartRepository
import com.catnip.rullfood.data.repository.CartRepositoryImpl
import com.catnip.rullfood.databinding.ActivityDetailBinding
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.utils.GenericViewModelFactory
import com.catnip.rullfood.utils.proceedWhen
import com.catnip.rullfood.utils.toCurrencyFormat
import com.chuckerteam.chucker.api.ChuckerInterceptor

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

//    private val viewModel: DetailViewModel by viewModel { parametersOf(intent.extras) }

    private val viewModel: DetailViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val chuckerInterceptor = ChuckerInterceptor(applicationContext)
        val service = RestaurantService.invoke(chuckerInterceptor)
        val apiDataSource = RestaurantDataSourceImpl(service)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource, apiDataSource)
        GenericViewModelFactory.create(
            DetailViewModel(repo, intent?.extras)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
        observeData()
        bindProduct(viewModel.menu)
    }

    private fun bindProduct(menu: Menu?) {
        menu?.let { item ->
            binding.ivPictMenu.load(item.productImgUrl) {
                crossfade(true)
            }
            binding.tvTextMenu.text = item.name
            binding.tvDescMenu.text = item.descOfMenu
            binding.tvHargaMenu.text = item.price.toCurrencyFormat()
        }
    }

    private fun observeData() {
        viewModel.priceLivedata.observe(this) {
            binding.tvTotalHarga.text = it.toCurrencyFormat()
        }
        viewModel.productCountLiveData.observe(this) {
            binding.tvSum.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivMinus.setOnClickListener {
            viewModel.minus()
        }
        binding.ivPlus.setOnClickListener {
            viewModel.add()
        }
        binding.cvCart.setOnClickListener {
            viewModel.addToCart()
        }
        binding.clDetail.setOnClickListener {
            navigateToMaps()
        }
    }

    private fun navigateToMaps() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://maps.app.goo.gl/h4wQKqaBuXzftGK77")
        )
        startActivity(intent)
    }

    companion object {
        const val EXTRA_FOOD = "EXTRA_FOOD"
        fun startActivity(
            context: Context,
            menu: Menu
        ) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_FOOD, menu)
            context.startActivity(intent)
        }
    }
}
