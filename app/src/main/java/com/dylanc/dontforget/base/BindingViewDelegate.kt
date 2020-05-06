package com.dylanc.dontforget.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate

abstract class BindingViewDelegate<T, B : ViewDataBinding> :
  ItemViewDelegate<T, BindingViewHolder<B>>() {

  lateinit var context: Context
  var onClickListener: ((position: Int, item: T) -> Unit)? = null

  override fun onCreateViewHolder(context: Context, parent: ViewGroup): BindingViewHolder<B> {
    this.context = context
    val itemView: View =
      when (val layout = getLayout(LayoutInflater.from(parent.context), parent)) {
        is View -> layout
        is Int -> LayoutInflater.from(parent.context).inflate(layout, parent, false)
        else -> throw ClassCastException("getLayout() return type must be int or View!")
      }
    val binding: B = DataBindingUtil.bind(itemView)!!
    onViewCreated(itemView,binding)
    return BindingViewHolder(itemView, binding)
  }

  protected open fun onViewCreated(itemView: View, binding: B) {
  }

  abstract fun getLayout(inflater: LayoutInflater, parent: ViewGroup): Any

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