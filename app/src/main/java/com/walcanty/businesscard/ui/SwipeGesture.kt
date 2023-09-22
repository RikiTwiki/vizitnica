package com.walcanty.businesscard.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.walcanty.businesscard.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeGesture(context: Context) : ItemTouchHelper.SimpleCallback(0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    val deleteColor = ContextCompat.getColor(context, android.R.color.holo_red_dark)
    val updateteColor = ContextCompat.getColor(context, R.color.teal_700)
    val deleteicon = R.drawable.ic_baseline_delete_24
    val updateicon = R.drawable.ic_baseline_edit_24
    val labelRight = "Редактировать"
    val labelLeft = "Удалить"

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteicon)
            .addSwipeRightBackgroundColor(updateteColor)
            .addSwipeRightActionIcon(updateicon)
            .addSwipeRightLabel(labelRight)
            .setSwipeRightLabelColor(Color.WHITE)
            .addSwipeLeftLabel(labelLeft)
            .setSwipeLeftLabelColor(Color.WHITE)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}