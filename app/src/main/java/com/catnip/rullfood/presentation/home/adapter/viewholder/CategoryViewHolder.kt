package com.catnip.rullfood.presentation.home.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.rullfood.core.ViewHolderBinder
import com.catnip.rullfood.databinding.ItemCategoriesBinding
import com.catnip.rullfood.model.Category
import com.catnip.rullfood.model.Menu

class CategoryViewHolder(
    private val binding: ItemCategoriesBinding,
    private val onItemClick: (Category) -> Unit
): RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Category>{
    override fun bind(item: Category) {
        binding.root.setOnClickListener{
            onItemClick.invoke(item)

        }
        binding.ivCategoryIcon.load(item.categoryImgUrl)
        binding.tvCategoryName.text = item.name
    }

}