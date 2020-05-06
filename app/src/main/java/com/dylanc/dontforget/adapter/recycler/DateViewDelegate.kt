package com.dylanc.dontforget.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.BindingViewDelegate
import com.dylanc.dontforget.base.BindingViewHolder
import com.dylanc.dontforget.databinding.RecyclerItemDateBinding

class DateViewDelegate : BindingViewDelegate<String, RecyclerItemDateBinding>() {
  override fun getLayout(inflater: LayoutInflater, parent: ViewGroup) =
    R.layout.recycler_item_date

  override fun onBindViewHolder(holder: BindingViewHolder<RecyclerItemDateBinding>, item: String) {
    holder.binding.tvDate.text = item
  }
}