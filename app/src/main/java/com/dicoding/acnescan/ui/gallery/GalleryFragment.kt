package com.dicoding.acnescan.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.acnescan.data.adapter.GalleryAdapter
import com.dicoding.acnescan.database.GalleryEntity
import com.dicoding.acnescan.databinding.FragmentGalleryBinding
import com.dicoding.acnescan.ui.camera.AnalysisActivity

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val galleryViewModel: GalleryViewModel by viewModels()
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        galleryAdapter = GalleryAdapter(
            onItemClick = { event ->
                val intent = Intent(requireContext(), AnalysisActivity::class.java).apply {
                    putExtra(AnalysisActivity.EXTRA_IMAGE_PATH, event.imagePath)
                }
                startActivity(intent)
            },
            onItemLongPress = { event ->
                // Menampilkan dialog konfirmasi untuk menghapus history berdasarkan ID
                showDeleteConfirmationDialog(event)
            }
        )

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = galleryAdapter

        // Observe history data
        galleryViewModel.gallery.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isNullOrEmpty()) {
                binding.historyRecyclerView.visibility = View.GONE
                binding.placeholderText.visibility = View.VISIBLE
            } else {
                binding.historyRecyclerView.visibility = View.VISIBLE
                binding.placeholderText.visibility = View.GONE
                galleryAdapter.submitList(historyList)
            }
        }

        // Tombol Hapus Semua dengan konfirmasi
        binding.buttonDeleteAll.setOnClickListener {
            showDeleteAllConfirmationDialog()  // Tampilkan dialog konfirmasi
        }
    }

    // Dialog konfirmasi untuk menghapus satu item berdasarkan ID
    private fun showDeleteConfirmationDialog(event: GalleryEntity) {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete this photo?")
            .setPositiveButton("Yes") { _, _ ->
                galleryViewModel.deleteHistoryById(event.id)  // Hapus item berdasarkan ID
            }
            .setNegativeButton("No", null)
            .show()
    }

    // Dialog konfirmasi untuk menghapus seluruh riwayat
    private fun showDeleteAllConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete all photo history?")
            .setPositiveButton("Yes") { _, _ ->
                galleryViewModel.deleteAllHistory()  // Hapus semua history
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        galleryViewModel.refreshHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}