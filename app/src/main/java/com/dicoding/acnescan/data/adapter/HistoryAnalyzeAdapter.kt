package com.dicoding.acnescan.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.acnescan.data.model.response.HistoryItem
import com.dicoding.acnescan.databinding.ItemHistoryAnalyzeBinding

class HistoryAnalyzeAdapter(private val historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAnalyzeAdapter.HistoryViewHolder>() {

    // ViewHolder untuk setiap item
    inner class HistoryViewHolder(private val binding: ItemHistoryAnalyzeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryAnalyzeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
