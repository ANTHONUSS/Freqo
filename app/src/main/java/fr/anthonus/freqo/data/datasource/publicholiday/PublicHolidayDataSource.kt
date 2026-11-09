package fr.anthonus.freqo.data.datasource.publicholiday

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object PublicHolidayDataSource {
    private const val BASE_URL = "https://calendrier.api.gouv.fr/jours-feries/"

    val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val json = Json { ignoreUnknownKeys = true }

    val client = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)

    }.build()

    val publicHolidayService: PublicHolidayService by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PublicHolidayService::class.java)
    }

}