@file:Suppress("unused")

package com.dylanc.dontforget.adapter.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
fun ImageView.loadUrl(
  url: String,
  placeHolder: Drawable? = null
) {
  Glide.with(context).load(url).placeholder(placeHolder).into(this)
}

@BindingAdapter(value = ["isVisible"], requireAll = false)
fun View.setVisible(visible: Boolean) {
  isVisible = visible
}

@BindingAdapter(value = ["showDrawable", "drawableShowed"], requireAll = false)
fun ImageView.showDrawable(
  showDrawable: Boolean,
  drawableShowed: Int
) {
  setImageResource(if (showDrawable) drawableShowed else android.R.color.transparent)
}

@BindingAdapter(value = ["textColor"], requireAll = false)
fun TextView.bindTextColor(textColorRes: Int) {
  setTextColor(ContextCompat.getColor(context, textColorRes))
}

@BindingAdapter(value = ["imageRes"], requireAll = false)
fun ImageView.setImageRes(@DrawableRes imageRes: Int) {
  setImageResource(imageRes)
}

@BindingAdapter(value = ["selected"], requireAll = false)
fun View.selected(select: Boolean) {
  isSelected = select
}

@BindingAdapter(value = ["adjustWidth"])
fun View.adjustWidth(adjustWidth: Int) {
  val params = layoutParams
  params.width = adjustWidth
  layoutParams = params
}

@BindingAdapter(value = ["adjustHeight"])
fun View.adjustHeight(adjustHeight: Int) {
  val params = layoutParams
  params.height = adjustHeight
  layoutParams = params
}

@BindingAdapter(value = ["onCheckedChange"])
fun CompoundButton.setOnCheckedChangeListener(onCheckedChange: (Boolean) -> Unit) {
  setOnCheckedChangeListener { _, isChecked ->
    onCheckedChange(isChecked)
  }
}