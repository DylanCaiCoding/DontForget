package com.dylanc.dontforget.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleListAdapter<T>(callback: DiffUtil.ItemCallback<T>) :
  ListAdapter<T, SimpleViewHolder>(callback) {

  lateinit var context: Context
  var onClickListener: ((position: Int, item: T) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
    val itemView: View =
      when (val layout = getLayout(LayoutInflater.from(parent.context), parent)) {
        is View -> layout
        is Int -> LayoutInflater.from(parent.context).inflate(layout, parent, false)
        else -> throw ClassCastException("getLayout() return type must be int or View!")
      }
    context = itemView.context
    return SimpleViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
    onBindViewHolder(holder, getItem(position))
  }

  abstract fun getLayout(inflater: LayoutInflater, parent: ViewGroup): Any

  abstract fun onBindViewHolder(holder: SimpleViewHolder, item: T)

  protected fun View.setOnClickListener(holder: RecyclerView.ViewHolder, item: T) {
    setOnClickListener {
      onClickListener?.invoke(holder.layoutPosition, item)
    }
  }

}