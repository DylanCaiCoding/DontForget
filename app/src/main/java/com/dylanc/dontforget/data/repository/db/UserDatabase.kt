package com.dylanc.dontforget.data.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dylanc.dontforget.data.bean.User
import com.dylanc.utilktx.application


val userDatabase: UserDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
  Room.databaseBuilder(application, UserDatabase::class.java, "user_db").build()
}

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
}