package com.example.mynotes.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityInterceptor @Inject constructor(@ApplicationContext private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!Extensions.isInternetAvailable(context)) {
            throw IOException(Constants.ErrorMessages.PLEASE_CHECK_YOUR_INTERNET_CONNECTIVITY)
        }
        return chain.proceed(chain.request())
    }
}