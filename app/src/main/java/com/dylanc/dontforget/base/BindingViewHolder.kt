package com.dylanc.dontforget.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Dylan Cai
 * @since 2020/4/28
 */
class BindingViewHolder<T : ViewBinding>(
  val binding: T
) : RecyclerView.ViewHolder(binding.root)