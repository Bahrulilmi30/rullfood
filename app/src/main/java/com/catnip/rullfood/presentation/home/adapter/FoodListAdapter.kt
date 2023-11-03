package com.catnip.rullfood.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.catnip.rullfood.core.ViewHolderBinder
import com.catnip.rullfood.databinding.ItemGridMenuBinding
import com.catnip.rullfood.databinding.ItemLinearMenuBinding
import com.catnip.rullfood.model.Menu
import com.catnip.rullfood.presentation.home.adapter.viewholder.FoodItemGridViewHolder
import com.catnip.rullfood.presentation.home.adapter.viewholder.FoodItemLinearViewHolder

class FoodListAdapter(
    var adapterLayoutMode: AdapterLayoutMode,
    private val onItemClick: (Menu) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    private val differ = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            AdapterLayoutMode.GRID.ordinal -> {
                FoodItemGridViewHolder(
                    binding = ItemGridMenuBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onItemClick
                )
            }
            else -> {
                FoodItemLinearViewHolder(
                    binding = ItemLinearMenuBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onItemClick
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Menu>).bind(differ.currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return adapterLayoutMode.ordinal
    }

    fun setData(menuData: List<Menu>) {
        differ.submitList(menuData)
    }

    fun refreshList() {
        notifyItemRangeChanged(0, differ.currentList.size)
    }
}
