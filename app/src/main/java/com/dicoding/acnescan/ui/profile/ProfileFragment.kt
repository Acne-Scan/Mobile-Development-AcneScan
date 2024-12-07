package com.dicoding.acnescan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.acnescan.databinding.FragmentProfileBinding
import com.dicoding.acnescan.ui.login.MainActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Menggunakan view binding untuk akses komponen UI
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set up aksi tombol logout
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        return binding.root
    }

    private fun showLogoutDialog() {
        // Membuat dialog konfirmasi logout
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmation Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Ya") { _, _ ->
                // Aksi logout: arahkan ke LoginActivity dan tutup ProfileFragment
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish() // Menutup aktivitas saat ini (ProfileFragment)
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                // Tutup dialog jika tidak logout
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}