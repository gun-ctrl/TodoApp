package com.example.todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.data.model.TagData
import com.example.todoapp.data.model.Todo

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
@Dao
interface TodoDao {
    //插入todo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoData(todo: Todo)
    //获取todo数据
    @Query("select * from todo_table")
    fun getTodoDatas():LiveData<List<Todo>>
    //删除todo
    @Delete
    suspend fun deleteTodoData(todo: Todo)
    //删除所有todo
    @Query("delete from todo_table")
    suspend fun deleteAllTodoDatas()
    //更新数据
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodoData(todo: Todo)

    /**
     * 标签操作
     */
    //插入标签
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagData: TagData)

    //获取所有标签
    @Query("select * from tag_table")
    fun getAllTags():LiveData<TagData>
    //删除标签
    @Delete
    fun deleteTag(tagData: TagData)

}