package com.catnip.rullfood.presentation.details

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.catnip.rullfood.R
import com.catnip.rullfood.data.network.api.service.RestaurantService
import com.catnip.rullfood.databinding.ActivityDetailBinding
import com.catnip.rullfood.model.Menu

class DetailActivity : AppCompatActivity() {
    private val binding : ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener{
            onBackPressed()
        }
    }

    companion object {
        const val EXTRA_FOOD = "EXTRA_FOOD"
         fun startActivity(
             context:Context,
             menu: Menu){
             val intent = Intent(context, DetailActivity::class.java)
             intent.putExtra(EXTRA_FOOD, menu)
             context.startActivity(intent)
         }

    }
}

