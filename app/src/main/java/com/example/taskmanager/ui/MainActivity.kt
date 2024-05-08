package com.example.taskmanager.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.classpractis.TaskDao
import com.example.classpractis.TaskDatabase
import com.example.taskmanager.R
import com.example.taskmanager.adapter.TaskAdapter
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.databinding.FragmentCreateTaskBinding
import com.example.taskmanager.model.Tasks
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar

class MainActivity : AppCompatActivity(), TaskAdapter.HandleClickListener {
    lateinit var binding: ActivityMainBinding

    private lateinit var taskDao: TaskDao
    private lateinit var taskDataBase: TaskDatabase
    private lateinit var taskAdapter:TaskAdapter

    private var selectedDate: String = ""
    private var selectedTime: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseBuilder()
        taskDao = taskDataBase.getUserDao()

        // Initializing task adapter with task list retrieved from DAO
        taskAdapter = TaskAdapter(this@MainActivity, taskDao.getTask())

        binding.apply {
            Glide.with(this@MainActivity)
                .load(R.drawable.first_note) //
                .into(noDataImage)
            noDataImage.visibility = View.GONE

            addTask.setOnClickListener {
                showBottomSheet(null,"add")
            }
        }

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                // Filtering task list based on search text entered
                val searchText = s.toString()
                val filteredList = taskDao.searchTasksByTitle(searchText)
                taskAdapter.searchTasks(filteredList)
            }
        })

        setupRecyclerView()

        if (taskAdapter.itemCount == 0) {

            binding.noDataImage.visibility = View.VISIBLE
            Glide.with(this)
                .load(R.drawable.first_note)
                .into(binding.noDataImage)

        } else {
            binding.noDataImage.visibility = View.GONE
        }
    }

    // Method to observe LiveData and update RecyclerView
    private fun setupRecyclerView() {

        binding.taskRecycler.apply {
            taskDao.getTaskLive().observe(this@MainActivity) {
                tasks -> taskAdapter.updateList(tasks) }
            adapter = taskAdapter
        }
    }

    // Method to build Room database
    private fun databaseBuilder() {
        taskDataBase = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "User_Contact")
            .allowMainThreadQueries().build()
    }

    // Click listener implementation for deleting a task
    override fun onDeleteClick(tasks: Tasks) {
        taskDao.deleteTask(tasks)
        taskAdapter.updateList(taskDao.getTask())
    }

//    6:00 AM - Wake up and do a 30-minute workout session (running or yoga).

    // Click listener implementation for updating a task
    override fun onUpdateClick(tasks: Tasks) {
        showBottomSheet(tasks,"update")
    }

    // Click listener implementation for completing a task
    override fun onCompleteClick(tasks: Tasks) {
        tasks.isComplete = true
        taskDao.updateTask(tasks)
    }

    // Method to show bottom sheet for adding or updating a task
    private fun showBottomSheet(task: Tasks?,updateOrAdd:String) {
    val bottomSheetBinding = FragmentCreateTaskBinding.inflate(layoutInflater)
    val bottomSheetView = bottomSheetBinding.root

    val bottomSheetDialog = BottomSheetDialog(this)
    bottomSheetDialog.setContentView(bottomSheetView)

    with(bottomSheetBinding) {
        taskDate.inputType = InputType.TYPE_NULL
        taskDate.setOnClickListener { showDatePickerDialog(taskDate) }

        taskTime.inputType = InputType.TYPE_NULL
        taskTime.setOnClickListener { showTimePickerDialog(taskTime) }


        when (updateOrAdd) {
            "update" -> {
                layoutTitle.text = "Update Task"
                layoutDis.text = "Fill the details below to update your task into your TODO"
                addTask.text = "Update Task"
            }

            "add" -> {
                layoutTitle.text = "Add a task"
                layoutDis.text = "Fill the details below to add a task into your TODO"
                addTask.text = "Add task"
            }

            else -> {}
        }

        if (task != null) {
            addTaskTitle.setText(task.taskTitle)
            addTaskDescription.setText(task.taskDescription)
            taskEvent.setText(task.event)
            taskDate.setText(task.date)
            taskTime.setText(task.taskTime)
        }

        // Click listener for adding or updating a task
        addTask.setOnClickListener {
            val inputTaskTitle = addTaskTitle.text.toString().trim()
            val inputTaskDescription = addTaskDescription.text.toString().trim()
            val inputTaskEvent = taskEvent.text.toString().trim()

            if (task == null) {
                // Validate input only when adding a new task
                if (inputTaskTitle.isEmpty() || inputTaskDescription.isEmpty() || inputTaskEvent.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
                    // Display an error message indicating missing data
                    Toast.makeText(this@MainActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val newTask = task ?: Tasks(0, "", "", "", false, "", "")
            newTask.apply {
                taskTitle = inputTaskTitle
                taskDescription = inputTaskDescription
                event = inputTaskEvent
                date = selectedDate
                taskTime = selectedTime
            }

            if (task == null) {
                binding.noDataImage.visibility=View.GONE
                taskDao.addTask(newTask)
            } else {
                taskDao.updateTask(newTask)
            }

            bottomSheetDialog.dismiss()
        }
    }

    bottomSheetDialog.show()
}

    private fun showDatePickerDialog(textDate:EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                selectedDate = "$dayOfMonth-$monthOfYear-$year"
                textDate.hint = selectedDate
            },
            year,
            month,
            day
        )
        // Show the DatePickerDialog
        datePickerDialog.show()
    }


    private fun showTimePickerDialog(taskTime:EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime = "$hourOfDay:$minute"
                taskTime.hint = selectedTime
            },
            hour,
            minute,
            false
        )
        // Show the TimePickerDialog
        timePickerDialog.show()
    }




}