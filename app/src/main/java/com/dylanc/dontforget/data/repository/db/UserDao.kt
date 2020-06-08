package com.dylanc.dontforget.data.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dylanc.dontforget.data.bean.User

@Dao
interface UserDao {

  @Query("SELECT * from user_table")
  suspend fun getUserList(): List<User>

  @Insert
  suspend fun insert(user: User)

  @Query("DELETE FROM user_table")
  suspend fun deleteAll()
}