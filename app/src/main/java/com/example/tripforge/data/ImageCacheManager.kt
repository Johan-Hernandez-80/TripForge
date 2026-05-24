package com.example.tripforge.data

import android.content.Context
import java.io.File
import coil3.SingletonImageLoader

class ImageCacheManager(private val context: Context) {

    fun getCacheDir(): File {
        return File(context.cacheDir, "trip_images").apply {
            if (!exists()) mkdirs()
        }
    }

    fun clearImageCache(context: Context) {
        try {
            val loader = SingletonImageLoader.get(context)
            loader.memoryCache?.clear()
            loader.diskCache?.clear()
            // Keep manual cleanup as fallback
            getCacheDir().deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCacheSizeInMB(): Double {
        val cacheDir = getCacheDir()
        return if (cacheDir.exists()) {
            cacheDir.walk().sumOf { it.length() } / (1024.0 * 1024.0)
        } else {
            0.0
        }
    }
}
