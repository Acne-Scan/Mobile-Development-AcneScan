package com.dicoding.acnescan.data.adapter.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.databinding.ItemProductHomeBinding

class ProductAdapter(private val onItemClicked: (DataItemProducts) -> Unit): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList: List<DataItemProducts> = emptyList()

    class ProductViewHolder(private val binding: ItemProductHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productsData: DataItemProducts, onItemClick: (DataItemProducts) -> Unit) {
            binding.productName.text = productsData.description

            Glide.with(binding.root.context)
                .load(productsData.image)
                .into(binding.productImage)

            // Klik untuk membuka link produk di browser
            binding.root.setOnClickListener {
                // Membuat AlertDialog
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Product Details")
                builder.setMessage("The product you want is in a third party application")
                builder.setPositiveButton("Yes") { _, _ ->
                    // Jika pengguna memilih "Ya", buka link produk
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(productsData.link))
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
        val binding = ItemProductHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount(): Int = productList.size

    fun submitList(products: List<DataItemProducts>) {
        productList = products
        notifyDataSetChanged()
    }
}