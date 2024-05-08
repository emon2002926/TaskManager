package com.example.classpractis
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.model.Tasks

@Database(entities = [Tasks::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun getUserDao():TaskDao
}
