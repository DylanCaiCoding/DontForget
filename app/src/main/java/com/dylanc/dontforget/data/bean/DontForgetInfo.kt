package com.dylanc.dontforget.data.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Dylan Cai
 * @since 2020/1/23
 */
@Entity
data class DontForgetInfo(
  @PrimaryKey val id: Int,
  val title: String,
  val content: String?,
  val date: Long,
  @ColumnInfo(name = "data_str")val dateStr: String,
  val priority: Int,
  val status: Int,
  val type: Int
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readInt(),
    parcel.readString()!!,
    parcel.readString(),
    parcel.readLong(),
    parcel.readString()!!,
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt()
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeInt(id)
    parcel.writeString(title)
    parcel.writeString(content)
    parcel.writeLong(date)
    parcel.writeString(dateStr)
    parcel.writeInt(priority)
    parcel.writeInt(status)
    parcel.writeInt(type)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<DontForgetInfo> {
    override fun createFromParcel(parcel: Parcel): DontForgetInfo {
      return DontForgetInfo(parcel)
    }

    override fun newArray(size: Int): Array<DontForgetInfo?> {
      return arrayOfNulls(size)
    }
  }
}