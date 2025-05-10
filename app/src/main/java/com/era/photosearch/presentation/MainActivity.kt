package com.era.photosearch.presentation

import android.view.Gravity
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.era.photosearch.R
import com.era.photosearch.base.BaseActivity
import com.era.photosearch.databinding.ActivityMainBinding
import com.era.photosearch.extension.showAlertDialog
import com.era.photosearch.model.ui.AlertInfo
import com.era.photosearch.util.HttpStatus
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.net.UnknownHostException

@AndroidEntryPoint
class MainActivity : BaseActivity<MainEvent, ActivityMainBinding, MainViewModel>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate
    override val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override suspend fun eventObserver() {
        viewModel.event.collect {
            when (it) {
                is MainEvent.HandleException -> showUnexpectedErrorDialog(it.e)
            }
        }
    }

    private fun showUnexpectedErrorDialog(e: Exception?) {
        var title = getString(R.string.alert)
        var description = getString(R.string.unexpected_error_description)
        when (e) {
            is UnknownHostException -> {
                title = getString(R.string.no_internet)
                description = getString(R.string.no_internet_description)
            }

            is HttpException -> {
                val code = e.code()
                when (code) {
                    HttpStatus.TOO_MANY_REQUESTS -> {
                        title = getString(R.string.too_many_requests)
                        description = getString(R.string.too_many_requests_description)
                    }

                    HttpStatus.MISSING_QUERY_PARAM -> return
                }
            }
        }
        navController.showAlertDialog(
            AlertInfo(
                title = title,
                description = description,
                positiveText = getString(R.string.got_it),
            )
        )
    }

    fun isLoading(isLoading: Boolean) {
        binding.rlLoading.isInvisible = !isLoading
    }

    override fun bindComponent() {
        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.findNavController()
            setSupportActionBar(binding.toolbar)
            setupActionBarWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}