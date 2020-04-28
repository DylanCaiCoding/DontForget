package com.dylanc.dontforget.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.BindingViewHolder
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.databinding.RecyclerItemInfoBinding
import com.dylanc.utilktx.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
class DontForgetInfoDelegate :
  ItemViewDelegate<DontForgetInfo, BindingViewHolder<RecyclerItemInfoBinding>>() {
  private lateinit var context: Context

  override fun onCreateViewHolder(
    context: Context,
    parent: ViewGroup
  ): BindingViewHolder<RecyclerItemInfoBinding> {
    this.context = context
    val itemView = LayoutInflater.from(context)
      .inflate(R.layout.recycler_item_info, parent, false)
    return BindingViewHolder(itemView, DataBindingUtil.bind(itemView)!!)
  }

  override fun onBindViewHolder(
    holder: BindingViewHolder<RecyclerItemInfoBinding>,
    item: DontForgetInfo
  ) {
    holder.binding.apply {
      tvTitle.text = item.title
      tvContent.text = item.content
    }
    holder.itemView.apply {
      isLongClickable = true
      setOnLongClickListener {
        showItemsDialog()
        true
      }
    }
  }

  private fun View.showItemsDialog() {
    MaterialAlertDialogBuilder(context)
      .setItems(arrayOf("修改", "删除")) { _, which ->
        when (which) {
          0 -> {
            toast("修改")
          }
          1 -> {
          }
          else -> {
          }
        }
      }
      .show()
  }
}