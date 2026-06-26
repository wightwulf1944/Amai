package i.am.shiro.amai.coil3

import coil3.intercept.Interceptor
import coil3.request.CachePolicy
import coil3.request.ImageResult
import i.am.shiro.amai.network.Nhentai

class PageInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val data = request.data

        if (data is PageCoilModel) {
            val newRequest = request.newBuilder()
                .data(Nhentai.imageBaseUrls.random() + data.path)
                .diskCacheKey(data.toString())
                .memoryCacheKey(data.toString())
                .memoryCachePolicy(CachePolicy.DISABLED)
                .build()

            return chain.withRequest(newRequest).proceed()
        }

        return chain.proceed()
    }
}