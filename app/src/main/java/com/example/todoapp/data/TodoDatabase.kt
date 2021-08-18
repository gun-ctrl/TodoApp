package com.example.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.data.dao.TagDao
import com.example.todoapp.data.dao.TodoDao
import com.example.todoapp.data.model.Tag
import com.example.todoapp.data.model.TagData
import com.example.todoapp.data.model.Todo
import kotlinx.coroutines.InternalCoroutinesApi


/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
@TypeConverters(TodoConverter::class)
@Database(
    entities = [Todo::class, TagData::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDatabase:RoomDatabase() {
    abstract fun getTodoDao(): TodoDao
    abstract fun getTagDao(): TagDao

    //创建单例对象
    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null
        fun getDatabase(context: Context):TodoDatabase{
            if (INSTANCE != null){
                return INSTANCE!!
            }
            //创建对象
            synchronized(this){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context,TodoDatabase::class.java,"todo.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}