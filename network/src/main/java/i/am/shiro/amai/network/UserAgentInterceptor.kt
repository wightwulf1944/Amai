package i.am.shiro.amai.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val USER_AGENT = "Amai/${BuildConfig.VERSION_NAME} (https://github.com/wightwulf1944/Amai)"

internal class UserAgentInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.request()
            .newBuilder()
            .header("User-Agent", USER_AGENT)
            .build()
            .let { chain.proceed(it) }
}