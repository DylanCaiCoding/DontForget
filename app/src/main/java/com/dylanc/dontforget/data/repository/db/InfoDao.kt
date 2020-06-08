package com.dylanc.dontforget.data.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dylanc.dontforget.data.bean.DontForgetInfo

@Dao
interface InfoDao {

  @Query("select * from info_table")
  suspend fun getAllInfo(): List<DontForgetInfo>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(infoList:List<DontForgetInfo>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertInfo(info: DontForgetInfo)

  @Update
  suspend fun updateInfo(info: DontForgetInfo)

  @Delete
  suspend fun deleteInfo(info: DontForgetInfo)
}