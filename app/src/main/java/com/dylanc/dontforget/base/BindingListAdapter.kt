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

abstract class BindingListAdapter<T, B : ViewDataBinding>(callback: DiffUtil.ItemCallback<T>) :
  ListAdapter<T, BindingViewHolder<B>>(callback) {

  lateinit var context: Context
  var onClickListener: ((position: Int, item: T) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<B> {
    val itemView: View =
      when (val layout = getLayout(LayoutInflater.from(parent.context), parent)) {
        is View -> layout
        is Int -> LayoutInflater.from(parent.context).inflate(layout, parent, false)
        else -> throw ClassCastException("getLayout() return type must be int or View!")
      }
    context = itemView.context
    return BindingViewHolder(itemView, DataBindingUtil.bind(itemView)!!)
  }

  override fun onBindViewHolder(holder: BindingViewHolder<B>, position: Int) {
    onBindViewHolder(holder, getItem(position))
  }

  abstract fun getLayout(inflater: LayoutInflater, parent: ViewGroup): Any

  abstract fun onBindViewHolder(holder: BindingViewHolder<B>, item: T)

  protected fun RecyclerView.ViewHolder.setOnItemClickListener(item: T) {
    itemView.setOnClickListener {
      onClickListener?.invoke(layoutPosition, item)
    }
  }

  protected fun View.setOnClickListener(holder: RecyclerView.ViewHolder, item: T) {
    setOnClickListener {
      onClickListener?.invoke(holder.layoutPosition, item)
    }
  }

}