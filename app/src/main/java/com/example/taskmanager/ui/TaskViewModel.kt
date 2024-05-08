package com.example.taskmanager.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.InvalidationTracker
import androidx.room.Room
import com.example.classpractis.TaskDao
import com.example.classpractis.TaskDatabase
import com.example.taskmanager.adapter.TaskAdapter
import com.example.taskmanager.model.Tasks

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private var applicationContext: Context = application

    private lateinit var taskDao: TaskDao
    private lateinit var taskDataBase: TaskDatabase
    private lateinit var taskAdapter: TaskAdapter

    init {
        databaseBuilder()
    }










//    var list = emptyList<Tasks>()
//
//    fun getTaskAdapter(): List<Tasks> {
//        val taskDao = taskDataBase.getUserDao()
//        val tasks = taskDao.getTask()
//        val taskLiveData = taskDao.getTaskLive()
//
//        // Observe changes in the database and update the list accordingly
//        taskLiveData.observeForever { tasks ->
//            tasks?.let {
//                list = it
//            }
//        }
//
//        return list
//    }



    fun getTaskList():List<Tasks>{
        val taskDao = taskDataBase.getUserDao()
        val task = taskDao.getTask()
       return task
    }



//
//    fun getTaskListLive(): LiveData<List<Tasks>> {
//        val taskDao = taskDataBase.getUserDao()
//        return taskDao.getTaskLive()
//    }




    fun updateTaskList(tasks: Tasks){
        taskDao = taskDataBase.getUserDao()
        taskDao.addTask(tasks)
    }

    private fun databaseBuilder(){
        taskDataBase = Room.databaseBuilder(applicationContext, TaskDatabase::class.java,"User_Contact")
            .allowMainThreadQueries().build()
    }



}