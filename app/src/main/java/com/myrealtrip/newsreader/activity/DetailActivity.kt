package com.myrealtrip.newsreader.activity

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.*
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.myrealtrip.newsreader.BR
import com.myrealtrip.newsreader.R
import com.myrealtrip.newsreader.databinding.ActivityDetailBinding
import com.myrealtrip.newsreader.model.Item
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        private val TAG = DetailActivity::class.java.simpleName
        private const val MAX_PROGRESS = 100
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setActionBar()

        if (intent.hasExtra("item")) {
            val item = intent.getSerializableExtra("item") as Item
            Log.i(TAG, item.toString())

            binding.setVariable(BR.item, item)
            if (!item.link.isNullOrEmpty()) {
                initWebView(item.link!!)
                setWebClient()
            }
        }
    }

    private fun setActionBar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "상세 페이지"
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(url: String) {
        web_view.loadUrl(url)
        val settings = web_view.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.domStorageEnabled = true

        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)

        web_view.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }

    private fun setWebClient() {
        web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress < MAX_PROGRESS && progressBar.visibility == ProgressBar.GONE) progressBar.visibility = ProgressBar.VISIBLE
                if (newProgress == MAX_PROGRESS) progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}