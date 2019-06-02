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
import eu.mobilebear.babylon.presentation.socialdetail.adapter.PostsAdapter
import eu.mobilebear.babylon.presentation.social.viewmodel.SocialViewModel
import eu.mobilebear.babylon.presentation.social.viewmodel.SocialViewModel.ScreenState
import eu.mobilebear.babylon.util.ViewModelFactory
import eu.mobilebear.babylon.util.state.NetworkStatus
import kotlinx.android.synthetic.main.activity_social_posts.*
import kotlinx.android.synthetic.main.view_status.*
import javax.inject.Inject

class SocialDetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var socialNavigator: SocialNavigator

    @Inject
    lateinit var postsAdapter: PostsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: SocialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_posts)

        initView()

        viewModel = viewModelFactory.create(SocialViewModel::class.java)
        viewModel.posts.observe(this, ScreenStateObserver())
        viewModel.getPosts()
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(this)
        postsRecyclerView.adapter = postsAdapter
        postsRecyclerView.layoutManager = layoutManager
        retryButton.setOnClickListener { viewModel.getPosts() }
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

    private fun updateViewForSuccessNetworkStatus(screenState: ScreenState) {
        hideLoadingView()

        postsAdapter.submitList(screenState.posts)
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

    private inner class ScreenStateObserver : Observer<ScreenState> {

        override fun onChanged(screenState: ScreenState?) {
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
