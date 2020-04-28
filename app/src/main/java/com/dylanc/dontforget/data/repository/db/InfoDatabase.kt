package com.dylanc.dontforget.data.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.dylanc.dontforget.data.bean.DontForgetInfo

val infoDb = Room.databaseBuilder(Utils.getApp(), InfoDatabase::class.java, "info_db").build()

@Database(entities = [DontForgetInfo::class], version = 1)
abstract class InfoDatabase : RoomDatabase() {
  abstract fun infoDao(): InfoDao
}