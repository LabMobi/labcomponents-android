package mobi.lab.componentsdemo.infrastructure.common.platform

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import mobi.lab.componentsdemo.domain.storage.SessionStorage
import mobi.lab.componentsdemo.infrastructure.common.http.CacheFactory
import mobi.lab.componentsdemo.infrastructure.common.http.HttpClientFactory
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.InputStream

/**
 * We're saving application's context here, so there is not much to leak
 */
@SuppressLint("StaticFieldLeak")
object ImageLoader {

    private lateinit var context: Context
    private var defaultPlaceholder: Drawable? = null
    private var configured: Boolean = false

    fun configure(
        context: Context,
        env: AppEnvironment,
        sessionStorage: SessionStorage,
        defaultPlaceholder: Drawable? = null
    ) {
        if (configured) {
            Timber.e("Configure called, but ImageLoader already configured")
            return
        }
        this.context = context.applicationContext
        this.defaultPlaceholder = defaultPlaceholder

        // Init Glide
        val loggingInterceptor = if (env.debug) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        } else {
            null
        }
        val client = HttpClientFactory.newInstance(
            cache = CacheFactory.newInstance(context, "ImageLoader"),
            interceptors = arrayOf(loggingInterceptor, HttpClientFactory.createAuthInterceptor(env, sessionStorage))
        )
        val factory = OkHttpUrlLoader.Factory(client)
        Glide.get(context).registry.replace(GlideUrl::class.java, InputStream::class.java, factory)

        configured = true
    }

    fun load(url: String, imageView: ImageView, options: Options = Options(), callback: Callback? = null) {
        processRequest(glide().load(url), options)
            .listener(wrapCallback(callback))
            .into(imageView)
    }

    fun load(resId: Int, imageView: ImageView, options: Options = Options(), callback: Callback? = null) {
        processRequest(glide().load(resId), options)
            .listener(wrapCallback(callback))
            .into(imageView)
    }

    fun loadAsBitmap(url: String, imageView: ImageView) {
        glide().asBitmap()
            .load(url)
            .into(imageView)
    }

    fun clear(imageView: ImageView) {
        glide().clear(imageView)
    }

    private fun glide(): RequestManager {
        return Glide.with(context)
    }

    private fun wrapCallback(callback: Callback?): RequestListener<Drawable>? {
        if (callback == null) {
            return null
        }
        return object : RequestListener<Drawable> {

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                val error = e ?: RuntimeException("Unknown image loading error")
                callback.onError(error)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                callback.onSuccess()
                return false
            }
        }
    }

    private fun processRequest(request: RequestBuilder<Drawable>, options: Options): RequestBuilder<Drawable> {
        return request.apply(options.toRequestOptions())
    }

    data class Options(
        val scaleType: ImageView.ScaleType? = null,
        val size: Size? = null,
        val placeholder: Drawable? = defaultPlaceholder
    ) {
        fun toRequestOptions(): RequestOptions {
            var requestOptions = RequestOptions().downsample(DownsampleStrategy.DEFAULT)
            when (scaleType) {
                null -> {
                    // Don't apply scaling
                }
                ImageView.ScaleType.CENTER_INSIDE -> {
                    requestOptions = requestOptions.centerInside()
                }
                ImageView.ScaleType.FIT_CENTER -> {
                    requestOptions = requestOptions.fitCenter()
                }
                else -> {
                    requestOptions = requestOptions.centerCrop()
                }
            }
            if (placeholder != null) {
                requestOptions = requestOptions.placeholder(placeholder)
            }
            return requestOptions
        }
    }

    data class Size(val width: Int, val height: Int)

    interface Callback {
        fun onSuccess()
        fun onError(error: Exception)
    }
}
