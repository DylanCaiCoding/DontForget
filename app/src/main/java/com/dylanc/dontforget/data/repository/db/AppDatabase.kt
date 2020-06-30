package com.dylanc.dontforget.data.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.User
import com.dylanc.utilktx.application


val appDatabase: AppDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
  Room.databaseBuilder(application, AppDatabase::class.java, "app_db").build()
}

@Database(entities = [User::class, DontForgetInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao

  abstract fun infoDao(): InfoDao
}