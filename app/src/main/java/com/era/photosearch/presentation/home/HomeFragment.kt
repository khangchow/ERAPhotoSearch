package com.era.photosearch.presentation.home

import android.graphics.Typeface
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentHomeBinding
import com.era.photosearch.extension.customSpan
import com.era.photosearch.extension.navigate
import com.era.photosearch.extension.onBottomReached
import com.era.photosearch.presentation.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeEvent, FragmentHomeBinding, HomeViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding) =
        FragmentHomeBinding::inflate
    override val viewModel: HomeViewModel by viewModels()
    private var recyclerScrollListener: RecyclerView.OnScrollListener? = null

    override suspend fun eventObserver() {

    }

    override fun bindComponent() {
        setUpActionBar()
        setUpResultText()
        setUpRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            (binding.rvPhoto.adapter as PhotoAdapter).refresh()
        }
    }

    private fun setUpResultText() {
        binding.tvResult.apply {
            viewModel.searchQuery.observe(viewLifecycleOwner) {
                isGone = it.isEmpty()
                binding.btnExplore.isGone = it.isNotEmpty()
                val original = getString(R.string.search_result, it)
                val start = original.indexOf(it)
                val content = SpannableString(original).customSpan(
                    context = requireContext(),
                    colorId = R.color.primary,
                    styleId = Typeface.BOLD,
                    startPosition = start,
                    endPosition = start + it.length
                )
                text = content
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvPhoto.apply {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    startPostponedEnterTransition()
                    return true
                }
            })
            adapter = PhotoAdapter { sharedView, data, transitionName ->
                val extras =
                    FragmentNavigatorExtras(sharedView to data.id.toString())
                navigate(
                    directions = HomeFragmentDirections.actionHomeFragmentToPhotoDetailsFragment(
                        data, transitionName
                    ),
                    rootFragment = this@HomeFragment,
                    extras = extras
                )
            }.apply {
                addLoadStateListener { loadStates ->
                    binding.apply {
                        recyclerScrollListener?.let { removeOnScrollListener(it) }
                        swipeRefreshLayout.isRefreshing = false
                        if (loadStates.refresh is LoadState.NotLoading) {
                            when {
                                (adapter?.itemCount
                                    ?: 0) > 0 && loadStates.source.prepend.endOfPaginationReached -> {
                                    // load more when no internet
                                    onBottomReached {
                                        (adapter as PhotoAdapter).retry()
                                    }.also { recyclerScrollListener = it }
                                }

                                else -> {
                                    // load success
                                    val isListEmpty =
                                        adapter?.itemCount == 0 && viewModel.searchQuery.value?.isNotEmpty() == true
                                    tvEmpty.isInvisible = !isListEmpty
                                }
                            }
                        } else {
                            val errorState = (loadStates.source.append as? LoadState.Error)
                                ?: (loadStates.source.prepend as? LoadState.Error)
                                ?: (loadStates.source.refresh as? LoadState.Error)
                                ?: (loadStates.refresh as? LoadState.Error)
                            errorState?.let { loadStateError ->
                                val throwable = loadStateError.error
                                mainViewModel.onException(throwable as? Exception)
                            }
                        }
                    }
                }
            }
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            viewModel.photos.observe(viewLifecycleOwner) {
                lifecycleScope.launch { (adapter as PhotoAdapter).submitData(it) }
            }
        }
    }

    private fun setUpActionBar() {
        val menuHost: MenuHost = requireActivity()
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_home, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_search -> {
                        navigate(
                            directions = HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                                viewModel.searchQuery.value.orEmpty()
                            ),
                            rootFragment = this@HomeFragment
                        )
                    }
                }
                return true
            }
        }
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun setResultListener() {
        setSearchResultListener()
    }

    private fun setSearchResultListener() {
        setFragmentResultListener(SearchFragment.SEARCH_REQUEST_KEY) { _, bundle ->
            val query = bundle.getString(SearchFragment.SEARCH_QUERY_BUNDLE_KEY).orEmpty()
            viewModel.onSearchQueryChanged(query)
        }
    }
}