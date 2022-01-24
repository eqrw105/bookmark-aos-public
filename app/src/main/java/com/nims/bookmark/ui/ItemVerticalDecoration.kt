package com.nims.bookmark.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nims.bookmark.R

class ItemVerticalDecoration(
    context: Context,
    private val side: Float,
    private val height: Float
): RecyclerView.ItemDecoration() {
    private val color = ContextCompat.getColor(context, R.color.gray)
    private val paint = Paint().apply { color = this@ItemVerticalDecoration.color }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val left = parent.paddingLeft + side
        val right = parent.width - parent.paddingRight - side
        val childCount = parent.childCount
        for (i in 0 until childCount-1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + height

            c.drawRect(left, top.toFloat(), right, bottom, paint)
        }
    }
}