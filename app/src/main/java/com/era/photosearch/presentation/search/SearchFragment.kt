package com.era.photosearch.presentation.search

import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentSearchBinding
import com.era.photosearch.model.entity.SearchQuery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.max

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
        viewModel.event.collect {
            when (it) {
                is SearchEvent.DeletedSearchQuery -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.deleted, it.query),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
            val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    (adapter as SearchQueryAdapter).apply {
                        notifyItemChanged(viewHolder.absoluteAdapterPosition)
                        viewModel.deleteSearchQuery(
                            (getSearchQueryAt(viewHolder.absoluteAdapterPosition) as SearchQuery).content
                        )
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val maxSwipeDistance = -200f
                    val clampedDX = max(dX, maxSwipeDistance)

                    val isCancelled = clampedDX == 0f && !isCurrentlyActive
                    if (isCancelled) {
                        c.drawRect(
                            itemView.right + clampedDX, itemView.top.toFloat(),
                            itemView.right.toFloat(), itemView.bottom.toFloat(),
                            Paint()
                        )
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            clampedDX,
                            dY,
                            actionState,
                            false
                        )
                        return
                    }

                    val deleteDrawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
                    val itemHeight = itemView.height

                    deleteDrawable?.apply {
                        val intrinsicWidth = intrinsicWidth
                        val intrinsicHeight = intrinsicHeight
                        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2

                        // Position icon relative to clampedDX
                        val iconLeft = itemView.right + clampedDX.toInt() / 2 - intrinsicWidth / 2
                        val iconRight = iconLeft + intrinsicWidth
                        val iconBottom = deleteIconTop + intrinsicHeight

                        setBounds(iconLeft, deleteIconTop, iconRight, iconBottom)
                        draw(c)
                    }

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        clampedDX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }


            }
            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
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