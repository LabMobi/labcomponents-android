package mobi.lab.componentsdemo.infrastructure.common.http

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.Cache
import java.io.File
import kotlin.math.max
import kotlin.math.min

internal object CacheFactory {

    private const val MIN_DISK_CACHE_SIZE = (1024 * 1024 * 5).toLong() //  5 mb
    private const val MAX_DISK_CACHE_SIZE = (1024 * 1024 * 200).toLong() // 200 mb

    fun newInstance(context: Context, dirName: String = "Cache"): Cache {
        val cacheDir = getCacheDir(context, dirName)
        return Cache(cacheDir, calculateDiskCacheSize(cacheDir))
    }

    private fun getCacheDir(context: Context, name: String): File {
        val cache = File(context.applicationContext.cacheDir, name)
        if (!cache.exists()) {
            cache.mkdirs()
        }
        return cache
    }

    @SuppressLint("UsableSpace")
    @Suppress("SwallowedException") // We don't need to handle these exceptions here, we just default to a minimum cache size
    private fun calculateDiskCacheSize(dir: File): Long {
        val available = try {
            // We want the space that's available now. Since we don't allocate any space for our cache, we don't care about allocatable bytes.
            dir.usableSpace
        } catch (ex: Exception) {
            MIN_DISK_CACHE_SIZE
        }
        return max(min(available, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE)
    }
}
