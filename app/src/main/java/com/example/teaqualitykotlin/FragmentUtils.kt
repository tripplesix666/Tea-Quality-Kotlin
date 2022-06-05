package com.example.teaqualitykotlin

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teaqualitykotlin.ui.favorite.FragmentFavoriteViewModel
import com.example.teaqualitykotlin.ui.home.HomeViewModel
import com.example.teaqualitykotlin.ui.productPage.TeaProductPageViewModel
import com.example.teaqualitykotlin.ui.search.SearchViewModel
import com.example.teaqualitykotlin.ui.shoppingCart.FragmentShoppingCartViewModel
import java.lang.IllegalStateException

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            HomeViewModel::class.java -> {
                HomeViewModel(app.teasService)
            }
            TeaProductPageViewModel::class.java -> {
                TeaProductPageViewModel(app.teasService)
            }
            FragmentFavoriteViewModel::class.java -> {
                FragmentFavoriteViewModel(app.teasService)
            }
            FragmentShoppingCartViewModel::class.java -> {
                FragmentShoppingCartViewModel(app.teasService)
            }
            SearchViewModel::class.java -> {
                SearchViewModel(app.teasService)
            }
            else -> {

                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator