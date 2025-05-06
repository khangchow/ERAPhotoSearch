package com.era.photosearch.presentation

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
            viewModel.photos.observe(this@MainActivity) {
                lifecycleScope.launch { (rv.adapter as PhotoAdapter).submitData(it) }
            }
        }
    }
}