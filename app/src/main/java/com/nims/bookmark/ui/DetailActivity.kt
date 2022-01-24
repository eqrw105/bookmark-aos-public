package com.nims.bookmark.ui

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nims.bookmark.R
import com.nims.bookmark.core.BindingActivity
import com.nims.bookmark.databinding.ActivityDetailBinding
import com.nims.bookmark.ext.*
import com.nims.bookmark.room.Path

class DetailActivity : BindingActivity<ActivityDetailBinding>() {

    companion object {
        val PATH_ITEM_KEY = "PathItemKey"
    }

    override fun getLayoutResId(): Int = R.layout.activity_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar(R.id.toolbar)
        addBack()
        intent.extras?.run {
            val path = this.getSerializable(PATH_ITEM_KEY) as? Path
            path?.let {
                replaceTitle(it.title)
                openDetail(it.url)
            }
        }
    }

    private fun openDetail(url: String) {
        showProgress()
        val webView = binding.webView
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webView.loadUrl(url)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (view?.progress == 100) {
                    hideProgress()
                }
                super.onPageFinished(view, url)
            }
        }
    }

    override fun onBackPressed() {
        val webView = binding.webView
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }
}