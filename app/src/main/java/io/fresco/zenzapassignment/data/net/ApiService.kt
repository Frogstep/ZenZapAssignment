package io.fresco.zenzapassignment.data.net

import io.fresco.zenzapassignment.data.net.schemas.GlobalQuoteResponse
import io.fresco.zenzapassignment.data.net.schemas.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("query/?function=SYMBOL_SEARCH")
    suspend fun query(
        @Query("keywords") searchString: String,
    ) : SearchResponse

    @GET("query/?function=GLOBAL_QUOTE&")
    suspend fun quote(
        @Query("symbol") symbol: String,
    ) : GlobalQuoteResponse


    companion object{
        private val interceptor =  HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        private val client =  OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(AuthInterceptor())
            .build()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.alphavantage.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun create() =  retrofit.create(ApiService::class.java)
    }
}