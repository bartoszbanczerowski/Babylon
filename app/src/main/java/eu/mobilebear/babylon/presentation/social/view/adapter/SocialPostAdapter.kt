package eu.mobilebear.babylon.presentation.social.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.mobilebear.babylon.R
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.presentation.social.view.SocialPostOnClickListener
import kotlinx.android.synthetic.main.item_post.view.*
import javax.inject.Inject

class SocialPostAdapter @Inject constructor() : ListAdapter<SocialPost, SocialPostsViewHolder>(DIFFER) {

    private lateinit var socialPostOnClickListener: SocialPostOnClickListener

    fun setListener(socialPostOnClickListener: SocialPostOnClickListener) {
        this.socialPostOnClickListener = socialPostOnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialPostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return SocialPostsViewHolder(view, socialPostOnClickListener)
    }

    override fun onBindViewHolder(holder: SocialPostsViewHolder, position: Int) {
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

class SocialPostsViewHolder(val view: View, private val socialPostOnClickListener: SocialPostOnClickListener) : RecyclerView.ViewHolder(view) {

    fun bind(post: SocialPost) {
        view.postItemCardView.setOnClickListener { socialPostOnClickListener.onSocialPostClicked(post.id, post.userId) }
        view.postItemDescription.text = post.body
        view.postItemTitle.text = post.title

        if (post.company != null && post.company!!.name.isNotEmpty()) {
            view.postItemUserName.text = post.username
        } else {
            view.postItemBy.visibility = View.GONE
        }

        if (post.company != null && post.company!!.name.isNotEmpty()) {
            view.postItemUserCompany.text = post.company?.name
        } else {
            view.postItemFrom.visibility = View.GONE
        }

        view.postItemUserPhone.text = post.phone
        view.postItemUserAddress.text = post.address?.city + " " + post.address?.zipcode + ", " + post.address?.suite + " " + post.address?.suite
        view.postItemUserAddress.setOnClickListener {
            if (post.address?.geoLocation?.lat != null && post.address?.geoLocation?.lng != null)
                socialPostOnClickListener.onSocialPostAddressClicked(
                    post.address?.geoLocation?.lat!!,
                    post.address?.geoLocation?.lng!!
                )
        }
        view.postItemUserWebsite.text = post.website
        view.postItemUserEmail.text = post.email
    }
}
