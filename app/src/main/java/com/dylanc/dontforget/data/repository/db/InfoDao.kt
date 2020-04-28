package com.dylanc.dontforget.data.repository.db

import androidx.room.*
import com.dylanc.dontforget.data.bean.DontForgetInfo

@Dao
interface InfoDao {

  @Insert
  fun insertInfo(info: DontForgetInfo)

  @Update
  fun updateInfo(info: DontForgetInfo)

  @Delete
  fun deleteInfo(info: DontForgetInfo)

  @Query("select * from dontforgetinfo")
  fun loadAllInfo(info: DontForgetInfo)
}