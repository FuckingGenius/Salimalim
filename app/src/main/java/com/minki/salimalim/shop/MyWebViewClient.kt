package com.minki.salimalim.shop

import android.net.http.SslError
import android.util.Log
import android.webkit.*

open class MyWebViewClient : WebViewClient() {

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        Log.v("왔냐","ff")
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        Log.v("왔냐","ss")
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        Log.v("왔냐","dd")
        handler!!.proceed()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view!!.loadUrl(request!!.url.toString())
        return true
    }
}
