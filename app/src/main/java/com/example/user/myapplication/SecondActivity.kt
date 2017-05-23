package com.example.user.myapplication

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.alibaba.fastjson.JSONArray
import com.example.user.utils.media.GildeTools.GlideUtils
import com.example.user.utils.weight.numal.ListSlideView
import org.jetbrains.anko.AlertDialogBuilder
import uk.co.senab.photoview.PhotoView


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
class SecondActivity : AppCompatActivity() ,ImageAdapter.OnRemoveListener{


    private  var listView : ListSlideView? = null
    private var adapter : ImageAdapter?= null
    private var itemUrls = mutableListOf<String>()
    private var iv_imageView :ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val mActionBar = supportActionBar
        mActionBar!!.setHomeButtonEnabled(true)
        mActionBar.setDisplayHomeAsUpEnabled(true)
        mActionBar.title = "图片详情"
        getData()
        initView()
    }

    private fun initView() {
        listView = findViewById(R.id.list_item) as ListSlideView
        iv_imageView = findViewById(R.id.iv_imageView) as ImageView
        adapter = ImageAdapter(itemUrls,this)
        listView!!.adapter = adapter
        adapter!!.setRemoveListener(this)

        listView!!.setOnItemClickListener { parent, view, position, id ->
            ShowBigImage(itemUrls[position])
//            iv_imageView!!.visibility = View.VISIBLE
//            GlideUtils.downLoadRoundTransform(this,itemUrls[position],iv_imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher)
        }
//        iv_imageView!!.setOnClickListener(View.OnClickListener { iv_imageView!!.visibility = View.GONE})
    }

    private fun getData() {
        val extra = intent.getStringExtra("hehe")
        val items: JSONArray  = JSONArray.parseArray(extra)
        for (i in 0..items!!.size - 1){
            val middle = items.getJSONObject(i).getString("middle")
            itemUrls.add(middle)
        }
    }

    override fun onRemoveItem(position: Int) {
        itemUrls.removeAt(position)
        adapter!!.notifyDataSetChanged()
        listView!!.slideBack()
    }

    private fun ShowBigImage(item: String){
        val service = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = service.defaultDisplay.width
        val height = service.defaultDisplay.height
        val builder = AlertDialogBuilder(ctx = this).builder
        val imageView = PhotoView(this)
        imageView.minimumWidth = width
        imageView.minimumHeight = height
        builder.setView(imageView)
        GlideUtils.downLoadRoundTransform(this,item,imageView,R.color.white,R.mipmap.ic_launcher)
        val dialog = builder.show()
        dialog.setOnDismissListener { dialog ->   dialog.dismiss() }
        imageView.setOnClickListener(View.OnClickListener { dialog.dismiss() })
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
