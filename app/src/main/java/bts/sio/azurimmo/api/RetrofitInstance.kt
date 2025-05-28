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

    // ✅ CORRIGÉ: Format de date pour correspondre au backend SQL
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd") // ✅ Format SQL date standard (sans heures pour les dates)
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