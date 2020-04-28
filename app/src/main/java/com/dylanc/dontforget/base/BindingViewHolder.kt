package com.dylanc.dontforget.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Dylan Cai
 * @since 2020/4/28
 */
class BindingViewHolder<T : ViewDataBinding>(itemView: View, val binding: T) :
  RecyclerView.ViewHolder(itemView)