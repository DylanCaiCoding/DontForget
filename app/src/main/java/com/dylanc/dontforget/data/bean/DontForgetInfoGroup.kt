package com.dylanc.dontforget.data.bean

data class DontForgetInfoGroup(
  val date: String,
  val list: MutableList<DontForgetInfo>
)