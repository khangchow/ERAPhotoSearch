package com.era.photosearch.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.era.photosearch.databinding.ItemSearchQueryBinding
import com.era.photosearch.model.entity.SearchQuery
import com.era.photosearch.presentation.search.SearchQueryAdapter.QueryViewHolder

class SearchQueryAdapter : PagingDataAdapter<SearchQuery, QueryViewHolder>(QUERY_COMPARATOR) {
    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        val searchQuery = getItem(position)
        if (searchQuery != null) holder.bind(searchQuery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        return QueryViewHolder(
            ItemSearchQueryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class QueryViewHolder(private val binding: ItemSearchQueryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchQuery: SearchQuery) {
            binding.tvQuery.text = searchQuery.content
        }
    }

    companion object {
        private val QUERY_COMPARATOR = object : DiffUtil.ItemCallback<SearchQuery>() {
            override fun areItemsTheSame(oldItem: SearchQuery, newItem: SearchQuery) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SearchQuery, newItem: SearchQuery) =
                oldItem == newItem
        }
    }
}