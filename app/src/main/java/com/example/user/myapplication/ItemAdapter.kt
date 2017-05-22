package com.example.user.myapplication

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alibaba.fastjson.JSONObject


/**
 * 项目名称：MyApplication
 * 类描述：ItemAdapter 描述:
 * 创建人：songlijie
 * 创建时间：2017/5/18 12:01
 * 邮箱:814326663@qq.com
 */
class ItemAdapter(val items: MutableList<JSONObject>?, val context: Context) : BaseAdapter() {
    private var mRemoveListener: OnRemoveListener? = null

    override fun getItem(position: Int): Any? {
        return items!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(context, R.layout.item_list, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag  as ViewHolder
        }
        holder.str.text = items!!.get(position).getString("title")
        holder.tvDelete.setOnClickListener(View.OnClickListener {
         if (mRemoveListener!! !=null){
             mRemoveListener!!.onRemoveItem(position)
            }
        })
        return v
    }

    class ViewHolder(viewItem: View) {
        var str: TextView = viewItem.findViewById(R.id.tv_item) as TextView
        var tvDelete: TextView = viewItem.findViewById(R.id.tvDelete) as TextView
    }

    interface OnRemoveListener {
        fun onRemoveItem(position: Int)
    }

    fun setRemoveListener(removeListener: OnRemoveListener) {
        this.mRemoveListener = removeListener
    }
}

