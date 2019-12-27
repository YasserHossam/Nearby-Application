package com.yasser.nearby.network.api

import com.yasser.nearby.network.RemoteNetworkException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException


class RetrofitProvider private constructor() {
    companion object {

        private lateinit var INSTANCE: Retrofit

        fun getRetrofitInstance(
            baseUrl: String,
            clientId: String,
            clientSecret: String,
            fouresquareApiVersion: String
        )
                : Retrofit {
            if (!this::INSTANCE.isInitialized) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()

                val userlessAuthenticationInterceptor = UserlessAuthenticationInterceptor(
                    clientId,
                    clientSecret)

                val foursquareVersioningInterceptor = FoursquareVersioningInterceptor(
                    fouresquareApiVersion)

                val networkInterceptor = NetworkInterceptor()

                val okHttpClient = OkHttpClient.Builder().apply {
                    addInterceptor(userlessAuthenticationInterceptor)
                    addInterceptor(foursquareVersioningInterceptor)
                    addInterceptor(httpLoggingInterceptor)
                    addInterceptor(networkInterceptor)
                }.build()

                val retrofitBuilder = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)

                INSTANCE = retrofitBuilder.build()
            }
            return INSTANCE
        }
    }

    private class UserlessAuthenticationInterceptor(
        val clientId: String,
        val clientSecret: String
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("client_id", clientId)
                .addQueryParameter("client_secret", clientSecret)
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    private class FoursquareVersioningInterceptor(val version: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("v", version)
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    private class NetworkInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response
            try {
                response = chain.proceed(chain.request())
            } catch (exc: Exception) {
                if (exc is SocketTimeoutException ||
                    exc is UnknownHostException ||
                    exc is SSLException)
                    throw RemoteNetworkException()
                else
                    throw exc
            }
            return response
        }

    }
}