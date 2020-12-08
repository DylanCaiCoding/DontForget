package com.dylanc.dontforget.ui.info_list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dylanc.dontforget.base.BindingViewHolder
import com.dylanc.dontforget.base.newBindingViewHolder
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.databinding.RecyclerItemInfoBinding

typealias InfoViewHolder = BindingViewHolder<RecyclerItemInfoBinding>

class InfoAdapter(
  private val onItemClick: (DontForgetInfo) -> Unit,
  private val onItemLongClick: (DontForgetInfo) -> Unit
) : ListAdapter<DontForgetInfo, InfoViewHolder>(InfoDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder =
    newBindingViewHolder(parent)

  override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
    holder.binding.apply {
      val item = getItem(position)
      tvTitle.text = item.title
      root.apply {
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
  }

  class InfoDiffCallback : DiffUtil.ItemCallback<DontForgetInfo>() {
    override fun areItemsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
      oldItem.title == newItem.title && oldItem.content == newItem.content
  }
}
