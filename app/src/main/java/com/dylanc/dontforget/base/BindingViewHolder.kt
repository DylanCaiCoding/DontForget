package com.dylanc.dontforget.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Dylan Cai
 * @since 2020/4/28
 */

inline fun <reified T : ViewBinding> newBindingViewHolder(parent: ViewGroup): BindingViewHolder<T> {
  val method = T::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
  val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as T
  return BindingViewHolder(binding)
}

class BindingViewHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)