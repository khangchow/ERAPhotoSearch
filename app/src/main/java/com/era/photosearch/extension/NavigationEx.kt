package com.era.photosearch.extension

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(directions: NavDirections, rootFragment: Fragment?) {
    val controller = findNavController()
    val currentDestination =
        (controller.currentDestination as? FragmentNavigator.Destination)?.className
            ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
    if (currentDestination == rootFragment?.javaClass?.name) {
        try {
            controller.navigate(directions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}