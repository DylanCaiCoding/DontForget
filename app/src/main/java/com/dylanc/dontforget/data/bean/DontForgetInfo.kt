package com.dylanc.dontforget.data.bean

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
@Parcelize
@Entity(tableName = "info_table")
data class DontForgetInfo(
  @PrimaryKey
  @ColumnInfo(name = "id")
  val id: Int,
  val title: String,
  val content: String?,
  val date: Long,
  @ColumnInfo(name = "data_str")
  val dateStr: String,
  val priority: Int,
  val status: Int,
  val type: Int
) : Parcelable