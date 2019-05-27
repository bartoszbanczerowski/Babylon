package eu.mobilebear.babylon.presentation.social.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.mobilebear.babylon.R
import eu.mobilebear.babylon.networking.response.responsedata.Post
import kotlinx.android.synthetic.main.item_post.view.*
import javax.inject.Inject

class PostsAdapter @Inject constructor() : ListAdapter<Post, PostsViewHolder>(DIFFER) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFFER = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class PostsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: Post) {
        view.postItemDescription.text = status.body
        view.postItemTitle.text = status.title
    }
}
