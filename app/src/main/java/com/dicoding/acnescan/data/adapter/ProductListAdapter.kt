package com.dicoding.acnescan.ui.products

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.databinding.ItemProductBinding

class ProductListAdapter : ListAdapter<DataItemProducts, ProductListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind(product: DataItemProducts) {
            // Menampilkan nama produk
            binding.productName.text = product.description

            // Menampilkan gambar produk menggunakan Glide
            Glide.with(binding.productImage.context)
                .load(product.image)
                .into(binding.productImage)

            // Klik untuk membuka link produk di browser
            binding.root.setOnClickListener {
                // Membuat AlertDialog
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Product Details")
                builder.setMessage("The product you want is in a third party application")
                builder.setPositiveButton("Yes") { _, _ ->
                    // Jika pengguna memilih "Ya", buka link produk
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product.link))
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
        // Inflate layout untuk item produk
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position) // Ambil item berdasarkan posisi
        holder.bind(product)
    }

    // DiffCallback untuk membandingkan perubahan pada data
    class ProductDiffCallback : DiffUtil.ItemCallback<DataItemProducts>() {
        override fun areItemsTheSame(oldItem: DataItemProducts, newItem: DataItemProducts): Boolean {
            return oldItem.recommendationId == newItem.recommendationId // Perbandingan berdasarkan ID unik produk
        }

        override fun areContentsTheSame(oldItem: DataItemProducts, newItem: DataItemProducts): Boolean {
            return oldItem == newItem // Perbandingan isi objek produk
        }
    }
}