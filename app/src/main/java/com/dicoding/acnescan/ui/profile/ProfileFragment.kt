package com.dicoding.acnescan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.appcompat.app.AlertDialog
import com.dicoding.acnescan.databinding.FragmentProfileBinding
import com.dicoding.acnescan.ui.login.LoginActivity
import com.dicoding.acnescan.util.SharedPrefUtil
import com.dicoding.acnescan.data.factory.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Gunakan ViewModelFactory untuk mendapatkan ProfileViewModel dengan repository dan context
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Menggunakan view binding untuk akses komponen UI
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Periksa apakah pengguna sudah login
        val token = SharedPrefUtil.getToken(requireContext())
        if (token.isNullOrEmpty()) {
            binding.btnGoToLogin.visibility = View.VISIBLE
            binding.cardView.visibility = View.GONE
        } else {
            binding.btnGoToLogin.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE
            // Jika sudah login, tampilkan username
            profileViewModel.fetchUsername(requireContext())
        }

        // Observasi perubahan nama pengguna
        profileViewModel.username.observe(viewLifecycleOwner, Observer { username ->
            binding.tvName.text = username // Menampilkan nama pengguna di TextView
        })

        // Set up aksi tombol logout
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        // Tombol menuju LoginActivity jika belum login
        binding.btnGoToLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        return binding.root
    }

    private fun showLogoutDialog() {
        // Membuat dialog konfirmasi logout
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmation Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Aksi logout: hapus data dari SharedPreferences dan kembali ke LoginActivity
                SharedPrefUtil.clear(requireContext()) // Menghapus token dan data pengguna
                Toast.makeText(requireContext(), "You have logged out", Toast.LENGTH_SHORT).show()

                // Pindah ke LoginActivity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish() // Menutup fragment saat ini
            }
            .setNegativeButton("No") { dialog, _ ->
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