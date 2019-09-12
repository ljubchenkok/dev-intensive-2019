package ru.skillbranch.devintensive.ui.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.utils.Utils

class ChatItemTouchHelperCallback(
    val adapter: ChatAdapter,
    private val isArchived: Boolean,
    val swipeListener: (ChatItem) -> Unit
) : ItemTouchHelper.Callback() {
    private val bgRec = RectF()
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val iconBounds = Rect()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is ItemTouchViewHelper) {
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START)
        } else {
            makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START)
        }

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
         swipeListener.invoke(adapter.items[viewHolder.adapterPosition])
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHelper){
         viewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is ItemTouchViewHelper){
            viewHolder.onItemCleared()
        }
        super.clearView(recyclerView, viewHolder)
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            val itemView = viewHolder.itemView
            drawBackground(c, itemView, dX, recyclerView.context)
            drawIcon(c, itemView, dX)

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawBackground(canvas: Canvas, itemView: View, dX: Float, context: Context) {
        with(bgRec){
            left = itemView.right.toFloat() + dX
            right = itemView.right.toFloat()
            top = itemView.top.toFloat()
            bottom = itemView.bottom.toFloat()
        }
        with(bgPaint){
            color = Utils.getColorFromAttribute(R.attr.colorSwipeBackground, context)
        }
        canvas.drawRect(bgRec, bgPaint)
    }

    private fun drawIcon(canvas: Canvas, itemView: View, dX: Float) {
        val icon = if(!isArchived) itemView.resources.getDrawable(R.drawable.ic_archive_black_24dp,itemView.context.theme)
        else  itemView.resources.getDrawable(R.drawable.ic_unarchive_black_24dp,itemView.context.theme)
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
        val margin = (itemView.bottom - itemView.top - iconSize)/2
        with(iconBounds){
            left = itemView.right + dX.toInt() + space
            right = itemView.right + dX.toInt() + iconSize+space
            top = itemView.top + margin
            bottom = itemView.bottom - margin
        }
        icon.bounds = iconBounds
        icon.draw(canvas)


    }

}

interface ItemTouchViewHelper {
    fun onItemSelected()
    fun onItemCleared()
}