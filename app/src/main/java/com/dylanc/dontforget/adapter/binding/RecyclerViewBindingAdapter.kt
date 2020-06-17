package com.dylanc.dontforget.adapter.binding

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter

@BindingAdapter(value = ["adapter", "emptyView"])
fun RecyclerView.setRecyclerAdapter(adapter: RecyclerView.Adapter<*>?, layoutId: Int = 0) {
  if (adapter != null) {
    this.adapter = adapter
  }
  val emptyView: View? = findEmptyView(this, layoutId)
  adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
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
        emptyView?.visibility = View.VISIBLE
      } else {
        emptyView?.visibility = View.GONE
      }
    }
  })
}

private fun findEmptyView(viewGroup: ViewGroup, layoutId: Int): View? {
  val emptyView = viewGroup.findViewById<View>(layoutId)
  return if (emptyView != null) {
    emptyView
  } else {
    val parent = viewGroup.parent
    if (parent != null && parent is ViewGroup) {
      findEmptyView(parent, layoutId)
    } else {
      null
    }
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