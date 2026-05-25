package com.example.tripforge.data

import android.content.Context
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.tripforge.BuildConfig
import com.example.tripforge.model.TripSummary
import coil3.SingletonImageLoader
import coil3.request.ImageRequest

data class PixabayResponse(
    val hits: List<PixabayImage>
)

data class PixabayImage(
    @SerializedName("webformatURL")
    val webformatURL: String,
    val user: String
)

interface PixabayApi {
    @GET("api/")
    suspend fun searchImages(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("per_page") perPage: Int = 3
    ): PixabayResponse
}

class ImageService {
    companion object {
        private val PIXABAY_API_KEY = BuildConfig.PIXABAY_API_KEY
        private const val PIXABAY_BASE_URL = "https://pixabay.com/"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(PIXABAY_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(PixabayApi::class.java)

    suspend fun fetchImageUrl(country: String): String? = withContext(Dispatchers.IO) {
        Log.d("TripForge", "Key length: ${PIXABAY_API_KEY.length}, value: '$PIXABAY_API_KEY'")

        return@withContext try {
            val response = api.searchImages(
                key = PIXABAY_API_KEY,
                query = country
            )
            Log.d("TripForge", "Hits: ${response.hits.size}, URL: ${response.hits.firstOrNull()?.webformatURL}")
            response.hits.firstOrNull()?.webformatURL
        } catch (e: Exception) {
            Log.e("TripForge", "Pixabay call failed: ${e.javaClass.simpleName} - ${e.message}", e)
            null
        }
    }

    /**
     * Preloads images for all trips.
     * If imageUrl is missing, it fetches it.
     * If imageUrl exists, it warms up the Coil cache.
     */
    suspend fun preloadTripImages(
        context: Context,
        trips: List<TripSummary>,
        onUpdateTrip: suspend (TripSummary) -> Unit
    ) = withContext(Dispatchers.IO) {
        trips.forEach { trip ->
            if (trip.imageUrl.isBlank()) {
                val url = fetchImageUrl(trip.location)
                if (url != null) {
                    onUpdateTrip(trip.copy(imageUrl = url))
                }
            } else {
                // Warm up the image cache
                val request = ImageRequest.Builder(context)
                    .data(trip.imageUrl)
                    .build()
                SingletonImageLoader.get(context).enqueue(request)
            }
        }
    }
}