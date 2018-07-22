package com.github.kiolk.chemistrytests.utils

import android.content.Context
import android.view.View
import android.webkit.WebView

object ChartHelper {
        val ENGLISH : String = "en"
        val RUSSIAN: String = "ru"
        val BELARUS : String = "be"
        val PERIODIC_TABLE_NAME : String = "table"
        val PERIODIC_TABLE_ENGLISH : String = "table_en"
        val PERIODIC_TABLE_BELARUS: String = "table_by"
        val PERIODIC_TABLE_RUSSIAN: String = "table_ru"
        val SOLUBILITY_CHART_NAME : String = "solubility"
        val SOLUBILITY_CHART_ENGLISH : String = "solubility_en"
        val SOLUBILITY_CHART_BELARUS : String = "solubility_by"
        val SOLUBILITY_CHART_RUSSIAN : String = "solubility_ru"

        fun showWebView(context: Context, pictureName: String, webView: WebView) {
                val localeName = context.resources.configuration.locale.language
                val fileName : String = when(pictureName){
                        PERIODIC_TABLE_NAME ->{
                                when(localeName){
                                        ENGLISH -> PERIODIC_TABLE_ENGLISH
                                        BELARUS -> PERIODIC_TABLE_BELARUS
                                        RUSSIAN -> PERIODIC_TABLE_RUSSIAN
                                        else -> PERIODIC_TABLE_ENGLISH
                                }
                        }
                        SOLUBILITY_CHART_NAME ->{
                                when(localeName){
//                                        ENGLISH -> SOLUBILITY_CHART_ENGLISH
//                                        BELARUS -> SOLUBILITY_CHART_BELARUS
//                                        RUSSIAN -> SOLUBILITY_CHART_RUSSIAN
                                        else -> SOLUBILITY_CHART_NAME
                                }
                        }
                        else -> {
                                return
                        }
                }
                val urlFolder = context.cacheDir?.canonicalPath
                val url: String = "file:///android_asset/"
                val data = "<body bgcolor=\"#000000\"><div class=\"centered-content\" align=\"middle\" ><img src=\"$fileName.png\"/></div></body>"
                webView.loadDataWithBaseURL(url, data, "text/html", "UTF-8", null)
                webView.settings?.builtInZoomControls = true
                webView.settings?.displayZoomControls = false
                webView.settings?.useWideViewPort = true
//                webView.settings?.loadWithOverviewMode = false
                webView.visibility = View.VISIBLE
        }

}