package com.github.kiolk.chemistrytests.utils

import android.content.Context
import android.view.View
import android.webkit.WebView

fun showWebView(context : Context, pictureName : String, webView: WebView){
        val urlFolder = context.cacheDir?.canonicalPath
        val url: String = "file:///android_asset/"
        val data = "<body bgcolor=\"#000000\"><div class=\"centered-content\" align=\"middle\" ><img src=\"$pictureName.png\"/></div></body>"
        webView.loadDataWithBaseURL(url, data, "text/html", "UTF-8", null)
        webView.settings?.builtInZoomControls = true
        webView.settings?.displayZoomControls = false
        webView.settings?.useWideViewPort = true
        webView.settings?.loadWithOverviewMode = false
        webView.visibility = View.VISIBLE
}