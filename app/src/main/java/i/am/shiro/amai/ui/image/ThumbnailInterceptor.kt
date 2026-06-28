package i.am.shiro.amai.ui.image

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import coil3.request.allowRgb565
import coil3.size.Precision
import i.am.shiro.amai.data.remote.Nhentai

class ThumbnailInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val data = request.data

        if (data is ThumbnailModel) {
            val newRequest = request.newBuilder()
                .data(Nhentai.thumbnailBaseUrls.random() + data.path)
                .diskCacheKey(data.toString())
                .memoryCacheKey(data.toString())
                .allowRgb565(true)
                .precision(Precision.INEXACT)
                .build()

            return chain.withRequest(newRequest).proceed()
        }

        return chain.proceed()
    }
}