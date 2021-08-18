package com.example.todoapp.data

import androidx.room.TypeConverter
import com.example.todoapp.data.model.Priority

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class TodoConverter {

    //将priority对象存入数据库时调用
    @TypeConverter
    fun priorityToString(priority: Priority):String{
        return priority.name
    }

    //从数据库中取出该数据时转化为Priority类型
    @TypeConverter
    fun stringToPriority(str:String):Priority{
        return Priority.valueOf(str)
    }
}