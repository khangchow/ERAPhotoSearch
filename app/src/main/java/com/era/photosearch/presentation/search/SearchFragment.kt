package com.era.photosearch.presentation.search

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchEvent, FragmentSearchBinding, SearchViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding) =
        FragmentSearchBinding::inflate
    override val viewModel: SearchViewModel by viewModels()
    private lateinit var searchView: SearchView

    override suspend fun eventObserver() {

    }

    private fun setUpActionBar() {
        val menuHost: MenuHost = requireActivity()
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
                val searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView
                searchView.apply {
                    searchItem.expandActionView()
//                        setQuery(pendingQuery, false)
                    setOnQueryTextFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) findNavController().navigateUp()
                    }
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?) = true

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

    override fun bindComponent() {
        setUpActionBar()
    }

    override fun setResultListener() {

    }
}