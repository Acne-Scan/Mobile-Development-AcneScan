package com.dicoding.acnescan.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.acnescan.ui.gallery.GalleryFragment
import com.dicoding.acnescan.ui.home.HomeFragment
import com.dicoding.acnescan.ui.products.ProductsFragment

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3 // Jumlah fragment

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> ProductsFragment()
            2 -> GalleryFragment()
//            3 -> ProfileFragment()
            else -> HomeFragment()
        }
    }
}
