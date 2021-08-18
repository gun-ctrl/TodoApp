package com.example.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
@Entity(tableName = "tag_table")
data class TagData (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val bgColor:String
    )