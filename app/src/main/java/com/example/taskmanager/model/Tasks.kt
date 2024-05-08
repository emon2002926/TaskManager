package com.example.taskmanager.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Specify default value for auto-generating primary key
    var taskTitle: String,
    var date: String,
    var taskDescription: String,
    var isComplete: Boolean,
    var taskTime: String,
    var event: String
): Parcelable
