package com.example.twitterminiapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.twitterminiapp.data.model.Tweet
import com.example.twitterminiapp.data.util.Resource
import com.example.twitterminiapp.domain.usecase.GetTimelineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TwitterViewModel(
    private val app: Application,
    private val getTimelineUseCase: GetTimelineUseCase
) : AndroidViewModel(app) {

    val tweets: MutableLiveData<Resource<List<Tweet>>> = MutableLiveData()

    fun getTimeline() = viewModelScope.launch(Dispatchers.IO) {
        tweets.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable(app)) {
                val apiResult = getTimelineUseCase.execute()
                tweets.postValue(apiResult)
            } else {
                tweets.postValue(Resource.Error("Internet is not available"))
            }
        } catch (e: Exception) {
            tweets.postValue(Resource.Error(e.message.toString()))
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) true
        }
        return false
    }
}