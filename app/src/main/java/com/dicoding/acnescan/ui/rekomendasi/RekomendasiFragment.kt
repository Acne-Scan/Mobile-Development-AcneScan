package com.dicoding.acnescan.ui.rekomendasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.acnescan.databinding.FragmentRekomendasiBinding

class RekomendasiFragment : Fragment() {

    private var _binding: FragmentRekomendasiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(RekomendasiViewModel::class.java)

        _binding = FragmentRekomendasiBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: RecyclerView = binding.productList
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView. = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}