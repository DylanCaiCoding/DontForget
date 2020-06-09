package com.dylanc.dontforget.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.binding.setVisible
import com.dylanc.dontforget.base.BindingListAdapter
import com.dylanc.dontforget.base.BindingViewHolder
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.databinding.RecyclerItemInfoBinding
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoAdapter(
  private val onItemClick: (DontForgetInfo) -> Unit
) : BindingListAdapter<DontForgetInfo, RecyclerItemInfoBinding>(InfoDiffCallback()) {

  override fun getLayout(inflater: LayoutInflater, parent: ViewGroup) =
    R.layout.recycler_item_info

  override fun onBindViewHolder(
    holder: BindingViewHolder<RecyclerItemInfoBinding>,
    item: DontForgetInfo
  ) {
    holder.binding.apply {
      tvTitle.text = item.title
      if (item.content.isNullOrBlank()) {
        tvContent.setVisible(false)
      } else {
        tvContent.setVisible(true)
        tvContent.text = item.content
      }
    }
    holder.itemView.apply {
      isLongClickable = true
      setOnLongClickListener {
        showItemsDialog(item)
        true
      }
      setOnClickListener {
        onItemClick(item)
      }
    }
  }

  private fun View.showItemsDialog(info: DontForgetInfo) {
    MaterialAlertDialogBuilder(context)
      .setItems(arrayOf("关闭提醒", "删除")) { _, which ->
        when (which) {
          0 -> {
          }
          1 -> {
            apiServiceOf<TodoApi>()
              .deleteTodo(info.id)
              .io2mainThread()
              .subscribe({
                val list = arrayListOf<DontForgetInfo>()
                list.addAll(currentList)
                list.remove(info)
                submitList(list)
              }, {})
          }
          else -> {
          }
        }
      }
      .show()
  }

  class InfoDiffCallback : DiffUtil.ItemCallback<DontForgetInfo>() {
    override fun areItemsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DontForgetInfo, newItem: DontForgetInfo) =
      oldItem.title == newItem.title && oldItem.content == newItem.content
  }
}
