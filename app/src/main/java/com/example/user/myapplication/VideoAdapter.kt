package com.example.user.myapplication

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.fastjson.JSONObject
import com.example.user.utils.media.GildeTools.GlideUtils

/**
 * 项目名称：MyApplication
 * 类描述：VideoAdapter 描述:
 * 创建人：songlijie
 * 创建时间：2017/5/23 17:01
 * 邮箱:814326663@qq.com
 */
class VideoAdapter(val items: MutableList<JSONObject>?, val context: Context) : BaseAdapter(), View.OnClickListener {
    private var mRemoveListener: OnRemoveListener? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: VideoAdapter.ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(context, R.layout.video_item, null)
            holder = VideoAdapter.ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as VideoAdapter.ViewHolder
        }
        val item = items!![position]
        holder.tv_peoples.text ="观看人数:" + item.getString("person_num")
        holder.tv_theme.text = item.getString("name")
        holder.tv_publisher.text = item.getJSONObject("userinfo").getString("nickName")
        GlideUtils.downLoadRoundTransform(context,item.getJSONObject("pictures").getString("img"),holder.iv_cover,R.mipmap.ic_launcher,R.mipmap.ic_launcher)
        holder.tvDelete.setOnClickListener(View.OnClickListener {
            if (mRemoveListener!! != null) {
                mRemoveListener!!.onRemoveItem(position)
            }
        })
        return v
    }

    override fun getItem(position: Int): Any? {
        return items!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items!!.size
    }

    override fun onClick(v: View?) {
    }

    interface OnRemoveListener {
        fun onRemoveItem(position: Int)
    }

    fun setRemoveListener(removeListener: OnRemoveListener) {
        this.mRemoveListener = removeListener
    }

    class ViewHolder(viewItem: View) {
        var iv_cover: ImageView = viewItem.findViewById(R.id.iv_cover) as ImageView
        var tv_theme: TextView = viewItem.findViewById(R.id.tv_theme) as TextView
        var tv_publisher: TextView = viewItem.findViewById(R.id.tv_publisher) as TextView
        var tv_peoples: TextView = viewItem.findViewById(R.id.tv_peoples) as TextView
        var tvDelete: TextView = viewItem.findViewById(R.id.tvDelete) as TextView
    }
}