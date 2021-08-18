package com.example.todoapp.fragment.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class MainViewModel(application: Application):AndroidViewModel(application) {
    //数据仓库
    private val repository = TodoRepository(application)
    //todo数据
    val todoDataList:LiveData<List<Todo>> = repository.getTodoDatas()

    fun insertTodoData(todo: Todo){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertTodoData(todo)
        }
    }
    //删除todo
    fun deleteTodoData(todo: Todo){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteTodoData(todo)
        }
    }
    //删除所有todo
    fun deleteAllTodoDatas(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllTodoDatas()
        }
    }
    //更新todo
    fun updateTodoData(todo: Todo){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateTodoData(todo)
        }
    }

}