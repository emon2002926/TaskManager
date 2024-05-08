package com.example.classpractis

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.taskmanager.model.Tasks

@Dao
interface TaskDao {
    @Upsert
    fun addTask(tasks: Tasks)

    @Delete
    fun deleteTask(task: Tasks)

    @Update
    fun updateTask(task: Tasks)

    @Query("SELECT * FROM Tasks WHERE taskTitle LIKE '%' || :searchQuery || '%'")
    fun searchTasksByTitle(searchQuery: String): List<Tasks>

    @Query("SELECT * FROM Tasks")
    fun getTaskLive():LiveData<List<Tasks>>


    @Query("SELECT * FROM Tasks")
    fun getTask():List<Tasks>
}