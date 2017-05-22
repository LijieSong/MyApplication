package com.example.user.myapplication

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.user.utils.media.GildeTools.GlideUtils

/**
 * 项目名称：MyApplication
 * 类描述：ImageAdapter 描述:
 * 创建人：songlijie
 * 创建时间：2017/5/18 17:57
 * 邮箱:814326663@qq.com
 */
class ImageAdapter(val items: MutableList<String>?, val context: Context) : BaseAdapter() {
    private var mRemoveListener: ImageAdapter.OnRemoveListener? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ImageAdapter.ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(context, R.layout.item_image_list, null)
            holder = ImageAdapter.ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag  as ImageAdapter.ViewHolder
        }
        GlideUtils.downLoadRoundTransform(context,items!![position],holder.iv_item,R.mipmap.ic_launcher,R.mipmap.ic_launcher)
        holder.tvDelete.setOnClickListener(View.OnClickListener {
            if (mRemoveListener!! !=null){
                mRemoveListener!!.onRemoveItem(position)
            }
        })
        return v
    }

    override fun getItem(position: Int): Any {
        return  items!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return  items!!.size
    }
    class ViewHolder(viewItem: View) {
        var iv_item: ImageView = viewItem.findViewById(R.id.iv_item) as ImageView
        var tvDelete: TextView = viewItem.findViewById(R.id.tvDelete) as TextView
    }
    interface OnRemoveListener {
        fun onRemoveItem(position: Int)
    }

    fun setRemoveListener(removeListener: OnRemoveListener) {
        this.mRemoveListener = removeListener
    }
}