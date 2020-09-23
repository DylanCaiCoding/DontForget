package com.dylanc.dontforget.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dylanc.dontforget.data.bean.DontForgetInfo
import com.dylanc.dontforget.data.bean.User


@Database(entities = [User::class, DontForgetInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao

  abstract fun infoDao(): InfoDao
}