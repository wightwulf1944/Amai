package i.am.shiro.amai.network;

import java.io.IOException;

import i.am.shiro.amai.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

final class UserAgentInterceptor implements Interceptor {

    private static final String USER_AGENT = String.format(
            "Amai/%s (https://github.com/wightwulf1944/Amai)", BuildConfig.VERSION_NAME);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("User-Agent", USER_AGENT)
                .build();
        return chain.proceed(request);
    }
}
