package com.dylanc.dontforget.adapter.recycler

import androidx.recyclerview.widget.DiffUtil
import com.dylanc.dontforget.data.bean.DontForgetInfo

class InfoDiffCallback : DiffUtil.ItemCallback<DontForgetInfo>() {
  override fun areItemsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
    oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
    oldItem.title == newItem.title && oldItem.content == newItem.content

}