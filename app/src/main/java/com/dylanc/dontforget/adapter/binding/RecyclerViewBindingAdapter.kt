package com.dylanc.dontforget.adapter.binding

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter

@Suppress("UNCHECKED_CAST")
@BindingAdapter(value = ["adapter", "list", "emptyView"])
fun <T : Any> RecyclerView.setRecyclerAdapter(
  adapter: RecyclerView.Adapter<*>?,
  list: List<T>?,
  EmptyViewId: Int = 0
) {
  if (adapter != null) {
    this.adapter = adapter
    val emptyView: View? = findViewInLayout(EmptyViewId)
    emptyView?.let { adapter.registerAdapterDataObserver(AdapterDataEmptyObserver(adapter, it)) }
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

private class AdapterDataEmptyObserver(
  private val adapter: RecyclerView.Adapter<*>,
  private val emptyView: View
) : RecyclerView.AdapterDataObserver() {

  override fun onChanged() {
    super.onChanged()
    checkEmpty()
  }

  override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
    super.onItemRangeInserted(positionStart, itemCount)
    checkEmpty()
  }

  override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
    super.onItemRangeRemoved(positionStart, itemCount)
    checkEmpty()
  }

  private fun checkEmpty() {
    if (adapter.itemCount == 0) {
      emptyView.visibility = View.VISIBLE
    } else {
      emptyView.visibility = View.GONE
    }
  }

}
