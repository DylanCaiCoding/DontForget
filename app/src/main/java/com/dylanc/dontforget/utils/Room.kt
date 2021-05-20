package com.dylanc.dontforget.utils

import androidx.room.Room
import androidx.room.RoomDatabase
import com.dylanc.longan.application

/**
 * @author Dylan Cai
 */

inline fun <reified T : RoomDatabase> roomDatabase(
  name: String,
  block: RoomDatabase.Builder<T>.() -> Unit = {}
) =
  Room.databaseBuilder(application, T::class.java, name).apply(block).build()