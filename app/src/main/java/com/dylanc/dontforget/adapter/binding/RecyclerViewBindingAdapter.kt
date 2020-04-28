package com.dylanc.dontforget.adapter.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter

@BindingAdapter(value = ["adapter"])
fun RecyclerView.setRecyclerAdapter(adapter: RecyclerView.Adapter<*>?) {
  if (adapter != null) {
    this.adapter = adapter
  }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter(value = ["list"])
fun <T : Any> RecyclerView.refreshList(list: List<T>?) {
  if (list == null) {
    return
  }
  val adapter = this.adapter
  if (adapter is ListAdapter<*, *>) {
    (adapter as ListAdapter<T, *>).submitList(list)
  } else if (adapter is MultiTypeAdapter) {
    adapter.items = list
    adapter.notifyDataSetChanged()
  }
}
