package com.dylanc.dontforget.adapter.recycler

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.dontforget.R
import com.dylanc.dontforget.adapter.binding.setVisible
import com.dylanc.dontforget.base.BindingViewHolder
import com.dylanc.dontforget.data.api.TodoApi
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.constant.KEY_EDIT_MODE
import com.dylanc.dontforget.data.constant.KEY_INFO
import com.dylanc.dontforget.data.constant.REQUEST_CODE_ADD_INFO
import com.dylanc.dontforget.data.repository.DontForgetInfoRepository
import com.dylanc.dontforget.databinding.RecyclerItemInfoBinding
import com.dylanc.dontforget.ui.main.info.InfoActivity
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.transformer.io2mainThread
import com.dylanc.utilktx.startActivityForResult
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
              .deleteTodo("${TodoApi.TODO}/delete/${info.id}/json")
              .io2mainThread()
              .subscribe({
                val list = arrayListOf<Any>()
                for (item in adapter.items) {
                  list.add(item)
                }
                list.remove(info)
                adapter.items = list
                adapter.notifyDataSetChanged()
              }, {})
          }
          else -> {
          }
        }
      }
      .show()
  }
}