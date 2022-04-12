package com.example.todo_app

import android.os.health.UidHealthStats
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.selects.select

@Dao
interface TodoDao{
    @Insert()
    suspend fun inserttask (tododb: Tododb):Long

    @Query("SELECT * from Tododb WHERE isFinished !=-1" )
    fun gettask():LiveData<List<Tododb>>

    @Query("Update Tododb set isFinished=1 where id =:uid")
     fun finishtask(uid:Long)

    @Query("Delete from Tododb where id =:uid")
    fun deletetask(uid:Long)

}
