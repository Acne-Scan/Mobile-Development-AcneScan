package com.dicoding.acnescan.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.acnescan.adapter.HistoryAdapter
import com.dicoding.acnescan.databinding.FragmentHistoryBinding
import com.dicoding.acnescan.ui.camera.AnalysisActivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: HistoryViewModel by viewModels()
    private lateinit var favoriteAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // observe error message
        favoriteViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        favoriteAdapter = HistoryAdapter { event ->
            val intent = Intent(requireContext(), AnalysisActivity::class.java)
            intent.putExtra("HISTORY_ID", event.id)
            startActivity(intent)
        }
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = favoriteAdapter

        // Observe favorite events from ViewModel
        favoriteViewModel.history.observe(viewLifecycleOwner) { favoriteList ->
            if (favoriteList.isNullOrEmpty()) {
                binding.historyRecyclerView.visibility = View.GONE
            }
            else {
                binding.historyRecyclerView.visibility = View.VISIBLE
                favoriteAdapter.submitList(favoriteList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}