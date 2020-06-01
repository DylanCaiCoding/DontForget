package com.dylanc.dontforget.data.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.utilktx.application

val infoDatabase: InfoDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
  Room.databaseBuilder(application, InfoDatabase::class.java, "info_db").build()
}

@Database(entities = [DontForgetInfo::class], version = 1, exportSchema = false)
abstract class InfoDatabase : RoomDatabase() {
  abstract fun infoDao(): InfoDao
}