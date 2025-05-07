package com.era.photosearch.presentation.home

import android.graphics.Typeface
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentHomeBinding
import com.era.photosearch.extension.customSpan
import com.era.photosearch.extension.navigate
import com.era.photosearch.presentation.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeEvent, FragmentHomeBinding, HomeViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding) =
        FragmentHomeBinding::inflate
    override val viewModel: HomeViewModel by viewModels()

    override suspend fun eventObserver() {

    }

    override fun bindComponent() {
        setUpActionBar()
        setUpResultText()
        setUpRecyclerView()
    }

    private fun setUpResultText() {
        binding.tvResult.apply {
            viewModel.searchQuery.observe(viewLifecycleOwner) {
                isGone = it.isEmpty()
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
            adapter = PhotoAdapter {
                navigate(
                    directions = HomeFragmentDirections.actionHomeFragmentToPhotoDetailsFragment(it),
                    rootFragment = this@HomeFragment
                )
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