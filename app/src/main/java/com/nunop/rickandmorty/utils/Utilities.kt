package com.nunop.rickandmorty.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import retrofit2.HttpException
import java.net.UnknownHostException

class Utilities {

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun checkStates(
        error: Throwable?,
        adapter: PagingDataAdapter<Any, RecyclerView.ViewHolder>,
        hasInternetConnection: Boolean?,
        loadStates: CombinedLoadStates,
        showLoading:(Boolean) -> Unit,
        showErrorGeneric:(Boolean) -> Unit,
        showErrorNoInternet:(Boolean) -> Unit,
    ) {
        if ((error is HttpException || error is UnknownHostException) &&
            adapter.snapshot().items.isEmpty() &&
            hasInternetConnection == false
        ) {
            showLoading(false)
            showErrorNoInternet(true)
            showErrorGeneric(false)
        } else if (error is Exception &&
            adapter.snapshot().items.isEmpty()
        ) {
            showLoading(false)
            showErrorGeneric(true)
            showErrorNoInternet(false)
        } else if (loadStates.mediator?.refresh is LoadState.Loading) {
            showErrorGeneric(false)
            showErrorNoInternet(false)
            showLoading(true)
        } else if (loadStates.mediator?.refresh is LoadState.NotLoading) {
            showErrorGeneric(false)
            showErrorNoInternet(false)
            showLoading(false)
        } else {
            showLoading(false)
            showErrorGeneric(false)
            showErrorNoInternet(false)
        }
    }
}