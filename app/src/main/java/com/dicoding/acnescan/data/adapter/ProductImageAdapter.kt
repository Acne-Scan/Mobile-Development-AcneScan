package com.dicoding.acnescan.ui.camera

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.acnescan.databinding.ItemProductBinding
import com.bumptech.glide.Glide
import com.dicoding.acnescan.data.model.response.Product

class ProductImageAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductImageAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            // Menampilkan nama produk
            binding.productName.text = product.name

            // Menampilkan gambar produk menggunakan Glide
            Glide.with(binding.productImage.context)
                .load(product.imageUrl)
                .into(binding.productImage)

            binding.root.setOnClickListener {
                // Intent untuk membuka URL produk di browser
                Log.d("ProductAdapter", "Opening link for: ${product.name}, URL: ${product.productUrl}")

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.productUrl))
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size
}