package com.dicoding.acnescan.ui.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CarouselItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        // Tambahkan margin di kiri dan kanan
        outRect.left = if (position == 0) margin else margin / 2
        outRect.right = if (position == itemCount - 1) margin else margin / 2
    }
}