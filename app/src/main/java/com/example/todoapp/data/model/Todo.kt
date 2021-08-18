package com.example.todoapp.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
@Parcelize   //实现序列化,使支持传递Custom Parcelize的数据
@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var title:String,
    var description: String, //内容
    var priority: Priority, //事务的重要性
    @Embedded
    var date: Date,//管理日期
    @Embedded
    var tag: Tag //管理标签
):Parcelable

/**
 * 标识事务的重要性
 */
enum class Priority{
    HIGH,MIDDLE,LOW
}

/**
 * 管理日期
 */
@Parcelize
data class Date(
    var year:Int,
    var month:Int,
    var day:Int
):Parcelable

/**
 * 管理标签
 */
@Parcelize
data class Tag(
    var text:String,
    var bgColor:String
):Parcelable