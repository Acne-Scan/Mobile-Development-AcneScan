package com.dicoding.acnescan.data.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.databinding.ItemProductHomeBinding

class ProductAdapter(private val onItemClicked: (DataItemProducts) -> Unit): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList: List<DataItemProducts> = emptyList()

    class ProductViewHolder(private val binding: ItemProductHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productsData: DataItemProducts, onItemClick: (DataItemProducts) -> Unit) {
//            binding.productName.text = productsData.name

            Glide.with(binding.root.context)
                .load(productsData.image)
                .into(binding.productImage)

            binding.root.setOnClickListener {
                onItemClick(productsData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount(): Int = productList.size

    fun submitList(articles: List<DataItemProducts>) {
        productList = articles
        notifyDataSetChanged()
    }
}