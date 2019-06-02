package eu.mobilebear.babylon.presentation.socialdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.mobilebear.babylon.R
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.networking.response.responsedata.Post
import kotlinx.android.synthetic.main.item_post.view.*
import javax.inject.Inject

class PostsAdapter @Inject constructor() : ListAdapter<SocialPost, SocialPostDetailViewHolder>(DIFFER) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialPostDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return SocialPostDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocialPostDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFFER = object : DiffUtil.ItemCallback<SocialPost>() {
            override fun areItemsTheSame(oldItem: SocialPost, newItem: SocialPost): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SocialPost, newItem: SocialPost): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class SocialPostDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: SocialPost) {
        view.postItemDescription.text = status.body
        view.postItemTitle.text = status.title
    }
}
