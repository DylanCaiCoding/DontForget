package com.dylanc.dontforget.data.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dylanc.dontforget.data.bean.DontForgetInfo

@Dao
interface InfoDao {

  @Query("select * from info_table")
  fun getAllInfo(): LiveData<List<DontForgetInfo>>

  @Insert
  fun insertInfo(info: DontForgetInfo)

  @Update
  fun updateInfo(info: DontForgetInfo)

  @Delete
  fun deleteInfo(info: DontForgetInfo)

  @Query("DELETE FROM info_table")
  fun deleteAll()
}