package com.example.todoapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.todoapp.data.model.TagData
import com.example.todoapp.data.model.Todo

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class TodoRepository(context: Context){
    private val todoDao = TodoDatabase.getDatabase(context).getTodoDao()
    private val tagDao = TodoDatabase.getDatabase(context).getTagDao()

    /**
     * TagDao
     */
    //插入Tag
    suspend fun insertTag(tag: TagData){
        tagDao.insertTag(tag)
    }

    //删除tag
    suspend fun deleteTagData(tag: TagData){
        tagDao.deleteTag(tag)
    }
    //删除所有todo
    fun getAllTags():LiveData<List<TagData>>{
        return tagDao.getAllTag()
    }

    /**
     * todoDao
     */
    //插入todo
    suspend fun insertTodoData(todo: Todo){
        todoDao.insertTodoData(todo)
    }
    //获取todo数据
    fun getTodoDatas():LiveData<List<Todo>>{
        return todoDao.getTodoDatas()
    }
    //删除todo
    suspend fun deleteTodoData(todo: Todo){
        todoDao.deleteTodoData(todo)
    }
    //删除所有todo
    suspend fun deleteAllTodoDatas(){
        todoDao.deleteAllTodoDatas()
    }
    //更新数据
    suspend fun updateTodoData(todo: Todo){
        todoDao.updateTodoData(todo)
    }

}