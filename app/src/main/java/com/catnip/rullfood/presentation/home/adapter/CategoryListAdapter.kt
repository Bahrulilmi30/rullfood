package com.catnip.rullfood.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.catnip.rullfood.core.ViewHolderBinder
import com.catnip.rullfood.databinding.ItemCategoriesBinding
import com.catnip.rullfood.model.Category
import com.catnip.rullfood.presentation.home.adapter.viewholder.CategoryViewHolder

class CategoryListAdapter(
    private val onItemClick :(Category)-> Unit
): RecyclerView.Adapter<CategoryViewHolder>() {
    private val differ = AsyncListDiffer(this, object: DiffUtil.ItemCallback<Category>(){

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    })
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            binding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        (holder as ViewHolderBinder<Category>).bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    fun setData(menuCategory: List<Category>) {
        differ.submitList(menuCategory)
    }
}