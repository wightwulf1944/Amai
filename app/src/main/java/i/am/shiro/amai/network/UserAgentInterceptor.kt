package i.am.shiro.amai.network

import i.am.shiro.amai.USER_AGENT
import okhttp3.Interceptor
import okhttp3.Response

internal class UserAgentInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.request()
            .newBuilder()
            .header("User-Agent", USER_AGENT)
            .build()
            .let { chain.proceed(it) }
}