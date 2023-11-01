package com.catnip.rullfood.presentation.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.catnip.rullfood.R
import com.catnip.rullfood.core.ViewHolderBinder
import com.catnip.rullfood.databinding.ItemCartBinding
import com.catnip.rullfood.databinding.ItemCartOrderBinding
import com.catnip.rullfood.model.Cart
import com.catnip.rullfood.utils.doneEditing

class CartListAdapter(
    private val cartListener : CartListener? = null
): RecyclerView.Adapter<ViewHolder>() {
    
    private val dataDiffer = 
        AsyncListDiffer(this,object : DiffUtil.ItemCallback<Cart>(){
            override fun areItemsTheSame(
                oldItem: Cart, 
                newItem: Cart
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Cart, 
                newItem: Cart
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })
    
    fun submitData(data : List<Cart>){
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (cartListener != null) CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), cartListener
        ) else CartOrderViewHolder(
            ItemCartOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }
}

class CartViewHolder(
    private val binding : ItemCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
        setClickListener(item)
    }

    private fun setCartData(item: Cart) {
        with(binding){
            binding.ivPictMenu.load(item.productImgUrl){
                crossfade(true)
            }
            tvSum.text = item.itemQuantity.toString()
            tvTextMenu.text = item.productName
            tvHargaMenu.text = (item.itemQuantity * item.productPrice).toString()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.etNotesItem.setText(item.itemNotes)
        binding.etNotesItem.doneEditing {
            binding.etNotesItem.clearFocus()
            val newItem = item.copy().apply {
                itemNotes = binding.etNotesItem.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }

    private fun setClickListener(item: Cart) {
        with(binding) {
            ivMinus.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item) }
            ivPlus.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item) }
            ivDelete.setOnClickListener { cartListener?.onRemoveCartClicked(item) }
        }
    }

}

class CartOrderViewHolder(
    private val binding: ItemCartOrderBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(item: Cart) {
        with(binding){
            binding.ivProductImage.load(item.productImgUrl){
                crossfade(true)
            }
            tvTotalQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.itemQuantity.toString()
                )
            tvProductName.text = item.productName
            tvProductPrice.text = (item.itemQuantity * item.productPrice).toString()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.tvNotes.text = item.itemNotes
    }

}

interface CartListener {
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}

