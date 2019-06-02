package eu.mobilebear.babylon.presentation.socialdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.mobilebear.babylon.R
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import kotlinx.android.synthetic.main.item_comment.view.*
import javax.inject.Inject

class CommentsAdapter @Inject constructor() : ListAdapter<Comment, SocialPostDetailViewHolder>(DIFFER) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialPostDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return SocialPostDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocialPostDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFFER = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class SocialPostDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(comment: Comment) {
        view.commentItemDescription.text = comment.body
        view.commentItemName.text = comment.title
        view.commentItemUserEmail.text = comment.email
    }
}
