package com.example.kode.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitClient @Inject constructor(): NetworkDataSource {

    private val baseUrl =
        "https://stoplight.io/mocks/kode-api/trainee-test/331141861/"

    private val contentType = "application/json".toMediaType()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofitNetworkApi = Retrofit.Builder()
            .baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(NetworkDataSource::class.java)

    override suspend fun getItems(): Items {
        return retrofitNetworkApi.getItems()
    }
}
//    // Serializing objects
//    val data = Project("kotlinx.serialization", "Kotlin")
//    val string = Json.encodeToString(data)
//    println(string) // {"name":"kotlinx.serialization","language":"Kotlin"}
//    // Deserializing back into objects
//    val obj = Json.decodeFromString<Project>(string)
//    println(obj) // Project(name=kotlinx.serialization, language=Kotlin)