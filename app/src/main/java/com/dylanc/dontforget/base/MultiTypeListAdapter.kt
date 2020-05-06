package com.dylanc.dontforget.base

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.drakeet.multitype.MultiTypeAdapter


class MultiTypeListAdapter(callback: DiffUtil.ItemCallback<Any>) : MultiTypeAdapter() {
  private val differ: AsyncListDiffer<Any> = AsyncListDiffer(this, callback)

  init {
    differ.submitList(items)
  }

  override fun getItemCount() = differ.currentList.size

  fun submitList(data: List<Any>) {
    items = data
    differ.submitList(data)
  }
}