package i.am.shiro.amai.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import i.am.shiro.amai.R
import i.am.shiro.amai.databinding.FragmentNhentai2Binding
import timber.log.Timber
import java.io.ByteArrayInputStream

class Nhentai2Fragment : Fragment(R.layout.fragment_nhentai_2) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentNhentai2Binding.bind(view)

        b.webView.settings.apply {
            domStorageEnabled = true
            javaScriptEnabled = true
            setGeolocationEnabled(false)
            allowContentAccess = false
            allowFileAccess = false
            builtInZoomControls = true
            displayZoomControls = false
        }
        b.webView.webViewClient = NhentaiClient()
        b.webView.loadUrl("https://nhentai.net/")

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (b.webView.canGoBack()) {
                b.webView.goBack()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    private class NhentaiClient : WebViewClient() {

        private val blacklist = setOf(
            "hw-cdn2.adtng.com",
            "a.adtng.com",
            "pxl.tsyndicate.com",
            "lcdn.tsyndicate.com",
            "tsyndicate.com",
            "cdn.tsyndicate.com"
        )

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Timber.d("rabbit loading ${request?.url}")
            return request?.url?.host != "nhentai.net"
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            val host = request?.url?.host
            return if (host in blacklist) {
                Timber.d("rabbit blocked $host")
                WebResourceResponse("text/html", "utf-8", "".byteInputStream())
            } else {
                Timber.d("rabbit passed $host")
                null
            }
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Timber.i("rabbit $url")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            Timber.i("rabbit $url")
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            Timber.i("rabbit $url")
        }
    }
}