package com.era.photosearch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<T, VB : ViewBinding>(
    private val checkItems: (old: T, new: T) -> Boolean,
    private val checkContents: (old: T, new: T) -> Boolean,
    private val checkPayload: ((old: T, new: T) -> Bundle?)? = null
) : RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<VB>>() {
    open val hasMultiViewTypes: Boolean = false
    private val asyncListDiffer: AsyncListDiffer<T> by lazy {
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
                return checkItems(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
                return checkContents(oldItem, newItem)
            }

            override fun getChangePayload(oldItem: T & Any, newItem: T & Any): Any? {
                return checkPayload?.invoke(oldItem, newItem)
            }
        })
    }

    protected open val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> VB?)? = null

    fun updateData(newData: List<T>, onDataUpdated: (() -> Unit)? = null) {
        asyncListDiffer.submitList(newData) {
            onDataUpdated?.invoke()
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun getItemViewType(position: Int): Int {
        return getViewType(asyncListDiffer.currentList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (hasMultiViewTypes) {
            createViewHolder(parent, layoutInflater, viewType)!!
        } else {
            BaseViewHolder(bindingInflater!!(layoutInflater, parent, false)!!)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        val item = asyncListDiffer.currentList[position]
        bindViewHolder(item, holder.binding, getItemViewType(position))
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<VB>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = asyncListDiffer.currentList[position]
        if (payloads.isEmpty()) {
            bindViewHolder(item, holder.binding, getItemViewType(position))
        } else {
            bindViewHolder(item, holder.binding, getItemViewType(position), payloads[0] as Bundle)
        }
    }

    open fun getViewType(item: T): Int {
        return 1
    }

    open fun createViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseViewHolder<VB>? {
        return null
    }

    abstract fun bindViewHolder(item: T, binding: ViewBinding, viewType: Int)
    open fun bindViewHolder(item: T, binding: ViewBinding, viewType: Int, bundle: Bundle) {}

    open class BaseViewHolder<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)
}

