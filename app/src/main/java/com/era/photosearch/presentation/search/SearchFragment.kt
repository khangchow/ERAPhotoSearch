package com.era.photosearch.presentation.search

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchEvent, FragmentSearchBinding, SearchViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding) =
        FragmentSearchBinding::inflate
    override val viewModel: SearchViewModel by viewModels()
    private lateinit var searchItem: MenuItem

    companion object {
        const val SEARCH_REQUEST_KEY = "SEARCH_REQUEST_KEY"
        const val SEARCH_QUERY_BUNDLE_KEY = "SEARCH_QUERY_BUNDLE_KEY"
    }

    override suspend fun eventObserver() {

    }

    private fun onSelectQuery(query: String) {
        mainViewModel.saveSearchQuery(query)
        setFragmentResult(
            SEARCH_REQUEST_KEY,
            bundleOf(SEARCH_QUERY_BUNDLE_KEY to query)
        )
        searchItem.collapseActionView()
    }

    private fun setUpActionBar() {
        val menuHost: MenuHost = requireActivity()
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
                searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView
                searchView.apply {
                    searchItem.expandActionView()
                    setQuery(viewModel.searchQuery.value, false)
                    setOnQueryTextFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) findNavController().navigateUp()
                    }
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            onSelectQuery(query.orEmpty())
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            viewModel.onQueryTextChanged(newText.orEmpty())
                            return true
                        }

                    })
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpRecyclerView() {
        binding.rvHistory.apply {
            adapter = SearchQueryAdapter { onSelectQuery(it) }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            viewModel.histories.observe(viewLifecycleOwner) {
                lifecycleScope.launch { (adapter as SearchQueryAdapter).submitData(it) }
            }
        }
    }

    override fun bindComponent() {
        setUpActionBar()
        setUpRecyclerView()
    }

    override fun setResultListener() {

    }
}