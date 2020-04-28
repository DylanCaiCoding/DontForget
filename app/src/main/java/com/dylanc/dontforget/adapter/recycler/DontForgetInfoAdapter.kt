package com.dylanc.dontforget.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.BindingListAdapter
import com.dylanc.dontforget.base.BindingViewHolder
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.databinding.RecyclerItemInfoBinding

class DontForgetInfoAdapter :
  BindingListAdapter<DontForgetInfo, RecyclerItemInfoBinding>(InfoDiffCallback()) {

  override fun getLayout(inflater: LayoutInflater, parent: ViewGroup) =
    R.layout.recycler_item_info

  override fun onBindViewHolder(holder: BindingViewHolder<RecyclerItemInfoBinding>, item: DontForgetInfo) {
    holder.binding.apply {
      tvTitle.text = item.title
      tvContent.text = item.content
    }
  }
}