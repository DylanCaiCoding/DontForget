package com.dylanc.dontforget.adapter.binding

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.dylanc.longan.observeDataEmpty

@Suppress("UNCHECKED_CAST")
@BindingAdapter(value = ["adapter", "list", "emptyView"])
fun <T : Any> RecyclerView.setRecyclerAdapter(
  adapter: RecyclerView.Adapter<*>?,
  list: List<T>?,
  EmptyViewId: Int = 0
) {
  if (adapter != null) {
    this.adapter = adapter
    findViewInLayout(EmptyViewId)?.let { adapter.observeDataEmpty(it) }
  }

  if (list != null) {
    if (adapter is ListAdapter<*, *>) {
      (adapter as ListAdapter<T, *>).submitList(list)
    } else if (adapter is MultiTypeAdapter) {
      adapter.items = list
      adapter.notifyDataSetChanged()
    }
  }
}

private fun ViewGroup.findViewInLayout(viewId: Int): View? {
  val emptyView = findViewById<View>(viewId)
  return if (emptyView != null) {
    emptyView
  } else {
    val parent = parent
    if (parent != null && parent is ViewGroup) {
      parent.findViewInLayout(viewId)
    } else {
      null
    }
  }
}

