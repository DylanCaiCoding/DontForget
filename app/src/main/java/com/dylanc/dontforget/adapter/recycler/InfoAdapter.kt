package com.dylanc.dontforget.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.SimpleListAdapter
import com.dylanc.dontforget.base.SimpleViewHolder
import com.dylanc.dontforget.data.bean.DontForgetInfo
import kotlinx.android.synthetic.main.recycler_item_info.view.*

class InfoAdapter(
  private val onItemClick: (DontForgetInfo) -> Unit,
  private val onItemLongClick: (DontForgetInfo) -> Unit
) : SimpleListAdapter<DontForgetInfo>(InfoDiffCallback()) {

  override fun getLayout(inflater: LayoutInflater, parent: ViewGroup) =
    R.layout.recycler_item_info

  override fun onBindViewHolder(holder: SimpleViewHolder, item: DontForgetInfo) {
    holder.itemView.apply {
      tv_title.text = item.title
      setOnClickListener {
        onItemClick(item)
      }
      isLongClickable = true
      setOnLongClickListener {
        onItemLongClick(item)
        true
      }
    }
  }

  class InfoDiffCallback : DiffUtil.ItemCallback<DontForgetInfo>() {
    override fun areItemsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
      oldItem.title == newItem.title && oldItem.content == newItem.content
  }
}
