package bts.sio.azurimmo.api

import ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:9008/"

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // ✅ CORRIGER: Configuration Gson pour gérer les dates correctement
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss") // Format pour les dates
        .setLenient() // Plus permissif pour le parsing
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // ✅ Utiliser le Gson configuré
            .client(client) // ✅ Ajouter le client avec logging
            .build()
            .create(ApiService::class.java)
    }
}