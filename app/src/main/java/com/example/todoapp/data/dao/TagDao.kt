package com.example.todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.data.model.TagData

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
@Dao
interface TagDao {
    //插入Tag
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagData: TagData)

    //删除tag
    @Delete
    suspend fun deleteTag(tagData: TagData)
    //读取所有todo
    @Query("select * from tag_table")
    fun getAllTag():LiveData<List<TagData>>
}