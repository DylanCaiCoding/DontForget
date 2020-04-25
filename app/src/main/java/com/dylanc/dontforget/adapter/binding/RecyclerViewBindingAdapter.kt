package com.dylanc.dontforget.adapter.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter

/**
 * Create by KunMinX at 20/4/18
 */
@BindingAdapter(value = ["app:adapter"])
fun RecyclerView.setRecyclerAdapter(adapter: RecyclerView.Adapter<*>?) {
  if (adapter != null) {
    this.adapter = adapter
  }
}

@BindingAdapter(value = ["app:list"])
fun RecyclerView.refreshList(list: List<Any>?) {
  val adapter = this.adapter
  if (list != null && adapter is MultiTypeAdapter) {
    adapter.items = list
    adapter.notifyDataSetChanged()
  }
}
