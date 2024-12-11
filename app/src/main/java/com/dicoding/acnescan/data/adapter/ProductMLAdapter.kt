package com.dicoding.acnescan.ui.camera

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.acnescan.databinding.ItemProductBinding
import com.bumptech.glide.Glide
import com.dicoding.acnescan.data.model.response.Product

class ProductMLAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductMLAdapter.ProductViewHolder>() {

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
                // Membuat AlertDialog
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Product Details")
                builder.setMessage("The product you want is in a third party application")
                builder.setPositiveButton("Yes") { _, _ ->
                    // Jika pengguna memilih "Ya", buka link produk
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.productUrl))
                    itemView.context.startActivity(intent)
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

                builder.show()
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