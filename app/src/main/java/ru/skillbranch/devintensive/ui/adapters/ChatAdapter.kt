package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_archive.*
import kotlinx.android.synthetic.main.item_chat_archive.view.*
import kotlinx.android.synthetic.main.item_chat_group.*
import kotlinx.android.synthetic.main.item_chat_group.view.*
import kotlinx.android.synthetic.main.item_chat_single.*
import kotlinx.android.synthetic.main.item_chat_single.view.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.utils.Utils

class ChatAdapter(private val listener: (ChatItem) -> Unit) :
    RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {

    companion object {
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    var items: List<ChatItem> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            SINGLE_TYPE -> SingleViewHolder(inflater.inflate(R.layout.item_chat_single, parent, false))
            GROUP_TYPE -> GroupViewHolder(inflater.inflate(R.layout.item_chat_group, parent, false))
            ARCHIVE_TYPE -> ArchiveItemViewHolder(inflater.inflate(R.layout.item_chat_archive, parent, false))
            else-> SingleViewHolder(inflater.inflate(R.layout.item_chat_single, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int = when (items[position].chatType) {
        ChatType.ARCHIVE -> ARCHIVE_TYPE
        ChatType.GROUP -> GROUP_TYPE
        ChatType.SINGLE -> SINGLE_TYPE
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(items[position], listener)

    }

    fun updateData(data: List<ChatItem>) {
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



    abstract inner class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: ChatItem, listener: (ChatItem) -> Unit)

    }


    inner class SingleViewHolder(itemView: View) : ChatItemViewHolder(itemView),
        ItemTouchViewHelper {
        override fun onItemSelected() {
            itemView.setBackgroundColor(Utils.getColorFromAttribute(R.attr.colorSelectedItemBackground, itemView.context))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Utils.getColorFromAttribute(R.attr.colorItemBackground, itemView.context))
        }


        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            if(item.avatar == null) {
                Glide.with(itemView)
                    .clear(iv_avatar_single)
                itemView.iv_avatar_single.setInitials(item.initials)
            } else {
                Glide.with(itemView)
                    .asBitmap()
                    .load(item.avatar)
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            iv_avatar_single.setImageBitmap(resource)
                            iv_avatar_single.setupBitmap()
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }
            itemView.tv_title_single.text = item.title
            itemView.tv_message_single.text = item.shortDescription
            sv_indicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
            with(tv_date_single) {
                if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_single) {
                if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            itemView.setOnClickListener {
                listener.invoke(item)
            }


        }

    }

    inner class GroupViewHolder(itemView: View) : ChatItemViewHolder(itemView),
        ItemTouchViewHelper {
        override fun onItemSelected() {
            itemView.setBackgroundColor(Utils.getColorFromAttribute(R.attr.colorSelectedItemBackground, itemView.context))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Utils.getColorFromAttribute(R.attr.colorItemBackground, itemView.context))
        }

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            itemView.iv_avatar_group.setInitials(item.title[0].toString())
            itemView.tv_title_group.text = item.title
            itemView.tv_message_group.text = item.shortDescription
            with(tv_message_author) {
                if (item.messageCount != 0) View.VISIBLE else View.GONE
                itemView.tv_message_author.text = "@${item.author}"
            }
            with(tv_date_group) {
                if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_group) {
                if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            itemView.setOnClickListener {
                listener.invoke(item)
            }


        }


    }

    inner class ArchiveItemViewHolder(itemView: View) : ChatItemViewHolder(itemView),
        ItemTouchViewHelper {
        override fun onItemSelected() {
            itemView.setBackgroundColor(Utils.getColorFromAttribute(R.attr.colorSelectedItemBackground, itemView.context))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Utils.getColorFromAttribute(R.attr.colorItemBackground, itemView.context))
        }

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            itemView.tv_title_archive.text = item.title
            itemView.tv_message_archive.text = item.shortDescription
            with(tv_message_author_archive) {
                if (item.author != null) View.VISIBLE else View.GONE
                text = "@${item.author}"
            }
            with(tv_date_archive) {
                if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_archive) {
                if (item.messageCount != 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            itemView.setOnClickListener {
                listener.invoke(item)
            }


        }


    }


}