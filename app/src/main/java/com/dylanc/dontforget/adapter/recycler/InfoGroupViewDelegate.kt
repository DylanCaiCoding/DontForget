package com.dylanc.dontforget.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dylanc.dontforget.R
import com.dylanc.dontforget.base.BindingViewDelegate
import com.dylanc.dontforget.base.BindingViewHolder
import com.dylanc.dontforget.data.bean.DontForgetInfoGroup
import com.dylanc.dontforget.databinding.RecyclerItemInfoGroupBinding

class InfoGroupViewDelegate :
  BindingViewDelegate<DontForgetInfoGroup, RecyclerItemInfoGroupBinding>() {

  private val infoAdapter = InfoAdapter()

  override fun getLayout(inflater: LayoutInflater, parent: ViewGroup) =
    R.layout.recycler_item_info_group

  override fun onViewCreated(itemView: View, binding: RecyclerItemInfoGroupBinding) {
    binding.recyclerView.adapter = infoAdapter
    binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
  }

  override fun onBindViewHolder(
    holder: BindingViewHolder<RecyclerItemInfoGroupBinding>,
    item: DontForgetInfoGroup
  ) {
    infoAdapter.submitList(item.list)
  }
}