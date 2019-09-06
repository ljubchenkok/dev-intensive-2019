package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_single.*
import kotlinx.android.synthetic.main.item_chat_single.sv_indicator
import kotlinx.android.synthetic.main.item_chat_single.view.*
import kotlinx.android.synthetic.main.item_user_list.*
import kotlinx.android.synthetic.main.item_user_list.view.*
import ru.skillbranch.devintensive.GlideApp
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.UserItem

class UserAdapter(val listener: (UserItem) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var items: List<UserItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(itemView)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(items[position], listener)

    fun updateData(data: List<UserItem>){
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition] == data[newItemPosition]

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].hashCode() == data[newItemPosition].hashCode()

        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(item: UserItem, listener: (UserItem) -> Unit) {
            if(item.avatar !=null){
                Glide.with(itemView)
                    .asBitmap()
                    .load(item.avatar)
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            iv_avatar_user.setImageBitmap(resource)
                            iv_avatar_user.setupBitmap()
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            } else {
                Glide.with(itemView)
                    .clear(iv_avatar_user)
                itemView.iv_avatar_user.setInitials(item.initials ?: "??")
            }
            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
            tv_user_name.text = item.fullName
            tv_last_activity.text = item.lastActivity
            iv_selected.visibility = if(item.isSelected) View.VISIBLE else View.GONE
            itemView.setOnClickListener { listener.invoke(item) }

        }
    }


}