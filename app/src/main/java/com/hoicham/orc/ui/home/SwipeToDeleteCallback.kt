package com.hoicham.orc.ui.home

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hoicham.orc.R

class SwipeToDeleteCallback(
    private val context: Context, private val onSwiped: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(
        context, R.drawable.ic_round_delete_white_24
    )?.apply {
        setTint(ContextCompat.getColor(context, R.color.error_red))
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped(viewHolder.bindingAdapterPosition)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
    ): Int {
        // Only allow swiping for scan items
        return if (viewHolder is ScanListAdapter.ScanItemViewHolder) {
            super.getSwipeDirs(recyclerView, viewHolder)
        } else {
            0
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) return

        val itemView = viewHolder.itemView
        val swipeProgress = -dX / itemView.width // Convert to 0..1 range

        // Update view alpha
        itemView.alpha = 1 - swipeProgress

        deleteIcon?.let { icon ->
            val itemHeight = itemView.bottom - itemView.top
            val iconTop = itemView.top + (itemHeight - icon.intrinsicHeight) / 2
            val iconMargin = (itemHeight - icon.intrinsicHeight) / 2
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            val iconBottom = iconTop + icon.intrinsicHeight

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(canvas)
        }

        super.onChildDraw(
            canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        )
    }
}
