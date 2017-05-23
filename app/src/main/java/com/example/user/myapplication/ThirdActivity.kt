package com.example.user.myapplication

import android.graphics.PixelFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class ThirdActivity : AppCompatActivity() {
    private var webview: WebView? = null
    private var videoPlayerStandard: JCVideoPlayerStandard? = null
    private var roomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.TRANSLUCENT)
        setContentView(R.layout.activity_third)
        val mActionBar = supportActionBar
        mActionBar!!.title = "直播详情"
        mActionBar!!.setHomeButtonEnabled(true)
        mActionBar.setDisplayHomeAsUpEnabled(true)
        getData()
        initView()
        initData()
    }

    private fun getData() {
        roomId = intent.getStringExtra("roomId")
    }

    private fun initData() {
        webview!!.settings.javaScriptEnabled = true
        webview!!.settings.javaScriptCanOpenWindowsAutomatically = true
        webview!!.loadUrl(Contants.HTMLURl + roomId)
        webview!!.setWebViewClient(HelloWebViewClient())
    }

    private fun initView() {
        webview = findViewById(R.id.webview) as WebView
        videoPlayerStandard = findViewById(R.id.JCVideoPlayerStandard) as JCVideoPlayerStandard
        videoPlayerStandard!!.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        JCVideoPlayer.releaseAllVideos()
    }

    //Web视图
    private inner class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
