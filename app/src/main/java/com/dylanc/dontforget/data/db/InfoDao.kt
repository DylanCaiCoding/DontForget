package com.dylanc.dontforget.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dylanc.dontforget.data.bean.DontForgetInfo

@Dao
interface InfoDao {

  @Query("select * from info_table")
  fun getAllInfoLiveData(): LiveData<List<DontForgetInfo>>

  @Query("select * from info_table")
  suspend fun getAll(): List<DontForgetInfo>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(infoList: List<DontForgetInfo>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertInfo(info: DontForgetInfo)

  @Delete
  suspend fun deleteInfo(info: DontForgetInfo)

  @Delete
  suspend fun deleteInfo(info: List<DontForgetInfo>)

  @Query("DELETE FROM info_table")
  suspend fun deleteAll()
}