package com.example.user.myapplication

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.GestureDetector
import android.view.View
import android.widget.RelativeLayout
import com.example.user.utils.media.GildeTools.GlideUtils
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.File

class ShowBigImageActivity : Activity() {

    private var iv_showbig: CustomView? = null
    private var show_big: PhotoView? = null
    private var url: String? = null
    private var savePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_big_image)
        getData()
        initView()
        initData()
    }

    private fun initData() {
        GlideUtils.downLoadRoundTransform(this@ShowBigImageActivity, url, show_big!!, R.color.white, R.color.white)
        OkHttpUtils(this).loadFile(url, savePath+"/" + System.currentTimeMillis().toString() + ".png", object : OkHttpUtils.DownloadCallBack {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onSuccess() {
                iv_showbig!!.finish()
                if (show_big!!.drawable == getDrawable(R.color.white) || show_big!!.drawable == getDrawable(R.mipmap.ic_launcher)) {
                    GlideUtils.downLoadRoundTransform(this@ShowBigImageActivity, savePath, show_big!!, R.color.white, R.color.white)
                }
            }

            override fun onProgress(progress: Int) {
                iv_showbig!!.setProgress(progress)
            }

            override fun onFailure() {
                iv_showbig!!.finish()
                show_big!!.setImageResource(R.mipmap.ic_launcher)
            }
        })
        show_big!!.onPhotoTapListener = PhotoViewAttacher.OnPhotoTapListener { view, x, y -> finish() }
    }

    private fun initView() {
        iv_showbig = findViewById(R.id.iv_showbig) as CustomView
        show_big = findViewById(R.id.show_big) as PhotoView
    }

    private fun getData() {
        url = intent.getStringExtra("url")
        val file = File(Environment.getExternalStorageDirectory().toString() + "/test/")
        if (!file.exists()) {
            file.mkdirs()
        }
        savePath = file.absolutePath
    }

    override fun onDestroy() {
        super.onDestroy()
        iv_showbig!!.finish()
    }
}
