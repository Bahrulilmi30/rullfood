package com.catnip.rullfood.presentation.home.adapter.viewholder

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.rullfood.core.ViewHolderBinder
import com.catnip.rullfood.databinding.ItemGridMenuBinding
import com.catnip.rullfood.databinding.ItemLinearMenuBinding
import com.catnip.rullfood.model.Menu

class FoodItemLinearViewHolder(
    private val binding : ItemLinearMenuBinding,
    private val onItemClick :(Menu)-> Unit
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu>{
    override fun bind(item: Menu) {
        binding.root.setOnClickListener{
            onItemClick.invoke(item)

        }
        binding.ivPictMenu.load(item.productImgUrl)
        binding.tvTextMenu.text = item.name
        binding.tvHargaMenu.text = item.price.toString()
    }

}

class FoodItemGridViewHolder(
    private val binding : ItemGridMenuBinding,
    private val onItemClick :(Menu)-> Unit
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu>{
    override fun bind(item: Menu) {
        binding.root.setOnClickListener{
            onItemClick.invoke(item)

        }
        binding.ivPictMenu.load(item.productImgUrl)
        binding.tvTextMenu.text = item.name
        binding.tvHargaMenu.text = item.price.toString()
    }

}