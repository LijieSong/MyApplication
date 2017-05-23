package com.example.user.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
import android.net.http.SslError
import android.webkit.SslErrorHandler


class ThirdActivity : AppCompatActivity() {
    private var webview: WebView? = null
    private var videoPlayerStandard: JCVideoPlayerStandard? = null
    private var roomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//        http://pl-hls3.live.panda.tv/live_panda/3f8c720caaeea924e362a8a708f6feea_small.m3u8
        webview!!.settings.javaScriptEnabled = true
        webview!!.settings.javaScriptCanOpenWindowsAutomatically = true
        webview!!.loadUrl(Contants.HTMLURl + roomId)
        webview!!.setWebViewClient(HelloWebViewClient())
//        videoPlayerStandard!!.setUp("http://pl-hls3.live.panda.tv/live_panda/3f8c720caaeea924e362a8a708f6feea_small.m3u8", videoPlayerStandard!!.IF_CURRENT_IS_FULLSCREEN, "直播")
//        videoPlayerStandard!!.ivStart.performClick()
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

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
            // super.onReceivedSslError(view, handler, error);
            // 接受所有网站的证书，忽略SSL错误，执行访问网页
            handler.proceed()
            //默认的处理方式，WebView变成空白页
            //handler.cancel();
            // 其他处理
            //handleMessage(Message msg);
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
