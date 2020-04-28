package com.dylanc.dontforget.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.dontforget.R
import com.dylanc.dontforget.data.bean.DontForgetInfo

/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
class DontForgetInfoDelegate :
  ItemViewDelegate<DontForgetInfo, DontForgetInfoDelegate.ViewHolder>() {

  override fun onCreateViewHolder(context: Context, parent: ViewGroup) = ViewHolder(
    LayoutInflater.from(parent.context)
      .inflate(R.layout.recycler_item_info, parent, false)
  )

  override fun onBindViewHolder(holder: ViewHolder, item: DontForgetInfo) {
    holder.tvTitle.text = item.title
    holder.tvContent.text = item.content
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
    val tvContent: TextView = itemView.findViewById(R.id.tv_content)
  }

}