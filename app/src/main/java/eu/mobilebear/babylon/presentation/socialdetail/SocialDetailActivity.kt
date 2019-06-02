package eu.mobilebear.babylon.presentation.socialdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import eu.mobilebear.babylon.R
import eu.mobilebear.babylon.presentation.social.navigator.SocialNavigator
import eu.mobilebear.babylon.presentation.socialdetail.adapter.CommentsAdapter
import eu.mobilebear.babylon.presentation.socialdetail.viewmodel.SocialDetailViewModel
import eu.mobilebear.babylon.util.ViewModelFactory
import eu.mobilebear.babylon.util.state.NetworkStatus
import kotlinx.android.synthetic.main.activity_social_detail_post.*
import kotlinx.android.synthetic.main.view_status.*
import javax.inject.Inject

class SocialDetailActivity : DaggerAppCompatActivity() {

    companion object {
        private const val DEFAULT_INVALID_VALUE = -1
    }

    @Inject
    lateinit var socialNavigator: SocialNavigator

    @Inject
    lateinit var commentsAdapter: CommentsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: SocialDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_detail_post)

        val postId = intent.getIntExtra(SocialNavigator.POST_ID, DEFAULT_INVALID_VALUE)
        val userId = intent.getIntExtra(SocialNavigator.USER_ID, DEFAULT_INVALID_VALUE)

        initView(postId, userId)

        viewModel = viewModelFactory.create(SocialDetailViewModel::class.java)
        viewModel.post.observe(this, ScreenStateObserver())
        viewModel.getPost(postId, userId)
    }

    private fun initView(postId: Int, userId: Int) {
        val layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.adapter = commentsAdapter
        commentsRecyclerView.layoutManager = layoutManager
        retryButton.setOnClickListener { viewModel.getPost(postId, userId) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateViewForSuccessNetworkStatus(screenState: SocialDetailViewModel.ScreenState) {
        hideLoadingView()
        val post = screenState.post

        postItemTitle.text = post?.title
        postItemDescription.text = post?.body
        postItemUserEmail.text = post?.email
        postItemUserWebsite.text = post?.website
        postItemUserName.text = post?.username
        postItemUserPhone.text = post?.phone

        if (post?.username != null && post.username.isNotEmpty()) {
            postItemUserName.text = post.username
        } else {
            postItemBy.visibility = View.GONE
        }

        if (post?.company != null && post.company.name.isNotEmpty()) {
            postItemUserCompany.text = post.company.name
        } else {
            postItemFrom.visibility = View.GONE
        }

        postItemUserAddress.text = post?.address?.city + " " + post?.address?.zipcode + ", " + post?.address?.suite + " " + post?.address?.suite
        postItemUserAddress.setOnClickListener {
                socialNavigator.goToMap(
                    post?.address?.geoLocation?.lat!!,
                    post.address.geoLocation.lng
                )
        }

        commentsAdapter.submitList(screenState.comments)
    }

    private fun updateViewForRunningNetworkStatus() {
        showLoadingView()
        hideErrors()
    }

    private fun updateViewForErrorNetworkStatus() {
        hideLoadingView()
        showGeneralError()
    }

    private fun updateViewForNoConnectionErrorNetworkStatus() {
        hideLoadingView()
        showNetworkError()
    }

    private fun showLoadingView() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingView() {
        progressBar.visibility = View.GONE
    }

    private fun hideErrors() {
        networkError.visibility = View.GONE
        generalError.visibility = View.GONE
        errorDescription.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun showNetworkError() {
        generalError.visibility = View.GONE
        networkError.visibility = View.VISIBLE
        errorDescription.text = getString(R.string.network_error)
        errorDescription.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE
    }

    private fun showGeneralError() {
        generalError.visibility = View.VISIBLE
        networkError.visibility = View.GONE
        errorDescription.text = getString(R.string.something_went_wrong)
        errorDescription.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE
    }

    private inner class ScreenStateObserver : Observer<SocialDetailViewModel.ScreenState> {

        override fun onChanged(screenState: SocialDetailViewModel.ScreenState?) {
            screenState ?: return

            when (screenState.networkStatus) {
                NetworkStatus.Running -> {
                    updateViewForRunningNetworkStatus()
                }
                NetworkStatus.Success -> {
                    updateViewForSuccessNetworkStatus(screenState)
                }
                NetworkStatus.NoConnectionError -> {
                    updateViewForNoConnectionErrorNetworkStatus()
                }
                is NetworkStatus.Error -> {
                    updateViewForErrorNetworkStatus()
                }
            }
        }
    }
}
