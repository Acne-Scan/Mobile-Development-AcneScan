package com.dicoding.acnescan.ui.history

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.acnescan.R
import com.dicoding.acnescan.data.model.response.DataItem
import com.dicoding.acnescan.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAnalyzeAdapter(private var historyList: List<DataItem?>) :
    RecyclerView.Adapter<HistoryAnalyzeAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        // Inflate the layout item_history.xml
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historyList.size


    fun updateData(newHistoryList: List<DataItem?>?) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Urutkan berdasarkan `created_at`
        historyList = newHistoryList?.sortedByDescending { item ->
            try {
                dateFormat.parse(item?.createdAt.toString())?.time
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()

        notifyDataSetChanged()
    }


    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem?) {
            // Set title and subtitle
            binding.historyTitle.text = item?.prediction  // Set the prediction as title

//            // Load the first image from the map using Glide
//            val imageUrl = item?.userPicture // Get the first image URL from the map
//            if (imageUrl != null) {
//                Glide.with(binding.root.context)
//                    .load(imageUrl)
//                    .into(binding.historyImg)
//            } else {
//                // Set a placeholder if no image is available
//                binding.historyImg.setImageResource(R.drawable.ic_notification)
//            }


            // Format `created_at` menjadi tanggal yang lebih ramah pengguna
            val formattedDate = formatTimestamp(item?.createdAt)
            binding.historyTime.text = formattedDate

            // Set the click listener for item click
            binding.root.setOnClickListener {
                // Create an Intent to navigate to DetailHistoryActivity
                val context = binding.root.context
                val intent = Intent(context, DetailHistoryActivity::class.java)

                Log.d("HistoryAnalyzeAdapter", "Intent to: $intent")

                // Pass necessary data to DetailHistoryActivity via intent
                intent.putExtra(DetailHistoryActivity.EXTRA_PREDICTION_TYPE, item?.prediction)
                intent.putExtra(DetailHistoryActivity.EXTRA_IMAGE_URI, item?.userPicture)
                intent.putExtra(DetailHistoryActivity.EXTRA_DESC_RECOMMENDATIONS, item?.recommendation)

                // Pass all product images and links
                item?.productImage?.let {
                    // Assuming productImage is a Map<String, String>, we pass it as a serializable
                    intent.putExtra(DetailHistoryActivity.EXTRA_PRODUCT_IMAGES, HashMap(it))
                }

                item?.productLinks?.let {
                    // Assuming productLinks is a Map<String, String>, we pass it as a serializable
                    intent.putExtra(DetailHistoryActivity.EXTRA_PRODUCT_LINKS, HashMap(it))
                }

                // Start DetailHistoryActivity
                context.startActivity(intent)
            }
        }

        private fun formatTimestamp(timestamp: String?): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Parsing UTC timestamp
                val date = inputFormat.parse(timestamp.toString())

                val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                outputFormat.format(date!!) // Format menjadi waktu lokal
            } catch (e: Exception) {
                "Invalid time"
            }
        }
    }
}