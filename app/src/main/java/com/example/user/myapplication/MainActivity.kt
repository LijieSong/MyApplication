package com.example.user.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.user.utils.weight.numal.ListSlideView
import com.example.user.utils.acache.ACache
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.example.user.utils.request.okhttp.OKHttpUtils
import com.example.user.utils.request.okhttp.Param
import com.example.user.utils.weight.swipyrefresh.SwipyRefreshLayout
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), ItemAdapter.OnRemoveListener, SwipyRefreshLayout.OnRefreshListener {


    private var listView: ListSlideView? = null
    private var srl_refresh: SwipyRefreshLayout? = null
    private var adapter: ItemAdapter? = null
    private var items = mutableListOf<JSONObject>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mActionBar = supportActionBar
        mActionBar!!.title = "图片列表"
        initView()
    }


    private fun initView() {
        listView = findViewById(R.id.list_item) as ListSlideView
        srl_refresh = findViewById(R.id.srl_refresh) as SwipyRefreshLayout
        getData(Contants.ID, Contants.TYPE)
        adapter = ItemAdapter(items, this)
        listView!!.adapter = adapter
        listView!!.setOnItemClickListener { parent, view, position, id ->
            val item = items!!.get(position)
            startActivity(Intent(MainActivity@ this, SecondActivity::class.java).putExtra("hehe", item.getJSONArray("list").toJSONString()))
        }
        adapter!!.setRemoveListener(this)
        srl_refresh!!.setOnRefreshListener(this)
    }

    override fun onRemoveItem(position: Int) {
        items!!.removeAt(position)
        adapter!!.notifyDataSetChanged()
        listView!!.slideBack()
    }

    private fun getData(id: Int, type: Int) {
        val format = SimpleDateFormat("yyyyMMddHHmmss")
        val format1 = format.format(Date())
        val params = ArrayList<Param>()
        params.add(Param("page", id.toString()))
        params.add(Param("type", type.toString()))
        params.add(Param("showapi_appid", Contants.APPID))
        params.add(Param("showapi_sign", Contants.SECRET))
        params.add(Param("showapi_timestamp", format1))
        params.add(Param("showapi_sign_method", "md5"))
        params.add(Param("showapi_res_gzip", "0"))
        OKHttpUtils(this).post(params, Contants.URl, object : OKHttpUtils.HttpCallBack {
            override fun onResponse(jsonObject: JSONObject) {
                val showapi_res_code = jsonObject.getInteger("showapi_res_code")
                when (showapi_res_code) {
                    0 -> {
                        val showapi_res_body = jsonObject.getJSONObject("showapi_res_body")
                        if (showapi_res_body != null || showapi_res_body!!.size !== 0) {
                            val pagebean = showapi_res_body!!.getJSONObject("pagebean")
                            ACache.get(this@MainActivity).put(Contants.KEY_JSONOBJECT + id, pagebean)
                            if (pagebean != null || pagebean!!.size !== 0) {
                                val contentlist = pagebean!!.getJSONArray("contentlist")
                                if (contentlist != null || contentlist!!.size !== 0) {
                                    for (i in 0..contentlist!!.size - 1) {
                                        val imageObjiect = contentlist!!.getJSONObject(i)
                                        items.add(imageObjiect)
                                    }
                                }
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                    }
                    else -> {
                        val jsonObject1 = ACache.get(this@MainActivity).getAsJSONObject(Contants.KEY_JSONOBJECT + id)
                        if (jsonObject1 != null) {
                            val contentlist = jsonObject1.getJSONArray("contentlist")
                            if (contentlist != null || contentlist!!.size !== 0) {
                                for (i in 0..contentlist!!.size - 1) {
                                    val imageObjiect = contentlist.getJSONObject(i)
                                    items.add(imageObjiect)
                                }
                            }
                        }
                        adapter!!.notifyDataSetChanged()
                        Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(errorMsg: String) {
                val jsonObject1 = ACache.get(this@MainActivity).getAsJSONObject(Contants.KEY_JSONOBJECT + id)
                if (jsonObject1 != null || jsonObject1!!.size !== 0) {
                    val contentlist = jsonObject1!!.getJSONArray("contentlist")
                    if (contentlist != null || contentlist!!.size !== 0) {
                        for (i in 0..contentlist!!.size - 1) {
                            val imageObjiect = contentlist.getJSONObject(i)
                            items.add(imageObjiect)
                        }
                    }
                }
                adapter!!.notifyDataSetChanged()
            }
        })
        srl_refresh!!.isRefreshing = false
    }


    override fun onLoad(p0: Int) {
        getData(p0.plus(1), Contants.TYPE)
    }

    override fun onRefresh(p0: Int) {
        items.clear()
        getData(1, Contants.TYPE)
    }

}
