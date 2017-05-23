package com.example.user.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.example.user.utils.acache.ACache
import com.example.user.utils.request.okhttp.OKHttpUtils
import com.example.user.utils.request.okhttp.Param
import com.example.user.utils.weight.numal.ListSlideView
import com.example.user.utils.weight.swipyrefresh.SwipyRefreshLayout
import java.text.SimpleDateFormat
import java.util.*

class VideoActivity : AppCompatActivity(), SwipyRefreshLayout.OnRefreshListener, VideoAdapter.OnRemoveListener {


    private var listView: ListSlideView? = null
    private var srl_refresh: SwipyRefreshLayout? = null
    private var items = mutableListOf<JSONObject>()
    private var adapter: VideoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        val mActionBar = supportActionBar
        mActionBar!!.title = "直播列表"
        initView()
        initData()
    }

    private fun initView() {
        listView = findViewById(R.id.list_item) as ListSlideView
        srl_refresh = findViewById(R.id.srl_refresh) as SwipyRefreshLayout
    }

    private fun initData() {
        getData(Contants.ID)
        adapter = VideoAdapter(items, this)
        listView!!.adapter = adapter
        listView!!.setOnItemClickListener { parent, view, position, id ->
            val item = items!!.get(position)
            val roomId = item.getString("id")
            startActivity(Intent(VideoActivity@ this, ThirdActivity::class.java).putExtra("roomId", roomId))
        }
        adapter!!.setRemoveListener(this)
        srl_refresh!!.setOnRefreshListener(this)
    }

    override fun onRemoveItem(position: Int) {
        items!!.removeAt(position)
        adapter!!.notifyDataSetChanged()
        listView!!.slideBack()
    }

    private fun getData(id: Int) {
        val format = SimpleDateFormat("yyyyMMddHHmmss")
        val format1 = format.format(Date())
        val params = ArrayList<Param>()
        params.add(Param("page", id.toString()))
        params.add(Param("showapi_appid", Contants.APPID))
        params.add(Param("showapi_sign", Contants.SECRET))
        params.add(Param("showapi_timestamp", format1))
        params.add(Param("showapi_sign_method", "md5"))
        params.add(Param("showapi_res_gzip", "0"))
        OKHttpUtils(this).post(params, Contants.VIDEOURl, object : OKHttpUtils.HttpCallBack {
            override fun onResponse(jsonObject: JSONObject) {
                val showapi_res_code = jsonObject.getInteger("showapi_res_code")
                when (showapi_res_code) {
                    0 -> {
                        val showapi_res_body = jsonObject.getJSONObject("showapi_res_body")
                        if (showapi_res_body != null || showapi_res_body!!.size !== 0) {
                            val code = showapi_res_body.getIntValue("ret_code")
                            when (code) {
                                0 -> {
                                    val pagebean = showapi_res_body!!.getJSONArray("data")
                                    ACache.get(this@VideoActivity).put(Contants.KEY_JSONOBJECTVIDEO + Contants.ID, pagebean)
                                    if (pagebean != null || pagebean!!.size !== 0) {
                                        for (i in 0..pagebean!!.size - 1) {
                                            val imageObjiect = pagebean!!.getJSONObject(i)
                                            items.add(imageObjiect)
                                        }
                                    }
                                }
                                else -> {
                                    val jsonObject1 = ACache.get(this@VideoActivity).getAsJSONArray(Contants.KEY_JSONOBJECTVIDEO + Contants.ID)
                                    if (jsonObject1 != null || jsonObject1!!.size !== 0) {
                                        for (i in 0..jsonObject1!!.size - 1) {
                                            val imageObjiect = jsonObject1!!.getJSONObject(i)
                                            items.add(imageObjiect)
                                        }
                                    }
                                }
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    }
                    else -> {
                        val jsonObject1 = ACache.get(this@VideoActivity).getAsJSONArray(Contants.KEY_JSONOBJECTVIDEO + Contants.ID)
                        if (jsonObject1 != null || jsonObject1!!.size !== 0) {
                            for (i in 0..jsonObject1!!.size - 1) {
                                val imageObjiect = jsonObject1!!.getJSONObject(i)
                                items.add(imageObjiect)
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                        Toast.makeText(this@VideoActivity, "请求失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(errorMsg: String) {
                val jsonObject1 = ACache.get(this@VideoActivity).getAsJSONArray(Contants.KEY_JSONOBJECTVIDEO + Contants.ID)
                if (jsonObject1 != null || jsonObject1!!.size !== 0) {
                    for (i in 0..jsonObject1!!.size - 1) {
                        val imageObjiect = jsonObject1!!.getJSONObject(i)
                        items.add(imageObjiect)
                    }
                }
                adapter!!.notifyDataSetChanged()
            }
        })
        srl_refresh!!.isRefreshing = false
    }


    override fun onLoad(p0: Int) {
        getData(p0.plus(1))
    }

    override fun onRefresh(p0: Int) {
        items.clear()
        getData(1)
    }
}