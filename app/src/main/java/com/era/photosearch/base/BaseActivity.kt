package com.era.photosearch.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<E : BaseEvent, VB : ViewBinding, VM : BaseViewModel<E>> :
    AppCompatActivity() {
    protected lateinit var binding: VB
    abstract val bindingInflater: ((LayoutInflater) -> VB)
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = bindingInflater(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpNavigation()
        bindComponent()
        bindEvent()
    }

    abstract fun bindComponent()
    abstract fun bindEvent()
    protected open fun setUpNavigation() {}
}