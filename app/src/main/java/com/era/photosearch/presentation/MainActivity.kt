package com.era.photosearch.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.era.photosearch.base.BaseActivity
import com.era.photosearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<MainEvent, ActivityMainBinding, MainViewModel>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate
    override val viewModel: MainViewModel by viewModels()

    override suspend fun eventObserver() {

    }

    override fun bindComponent() {
        binding.apply {
            rv.apply {
                adapter = PhotoAdapter()
                layoutManager =
                    GridLayoutManager(this@MainActivity, 2, GridLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                setItemViewCacheSize(20)
            }
            viewModel.photos.observe(this@MainActivity) {
                lifecycleScope.launch { (rv.adapter as PhotoAdapter).submitData(it) }
            }
        }
    }
}