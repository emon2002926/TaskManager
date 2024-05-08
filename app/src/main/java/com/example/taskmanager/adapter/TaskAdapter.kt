package com.example.taskmanager.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ItemTaskBinding
import com.example.taskmanager.model.Tasks
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(var listener: HandleClickListener, var task: List<Tasks>):RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    var dateFormat = SimpleDateFormat("EE dd MMM yyyy", Locale.US)
    var inputDateFormat = SimpleDateFormat("dd-M-yyyy", Locale.US)

    inner class ViewHolder(var binding: ItemTaskBinding):RecyclerView.ViewHolder(binding.root)

    fun updateList(newList: List<Tasks>) {
        task = newList
        notifyDataSetChanged()
    }

    fun searchTasks(newList: List<Tasks>) {
        task = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =task.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskList = task[position]

        holder.binding.apply {
            title.text = taskList.taskTitle
            description.text = taskList.taskDescription
            status.text = if (taskList.isComplete) "COMPLETED" else "UPCOMING"

            // Displaying formatted time
            try {
                val inputTime = SimpleDateFormat("HH:mm", Locale.US).parse(taskList.taskTime)
                val formattedTime = SimpleDateFormat("hh:mm a", Locale.US).format(inputTime)
                time.text = formattedTime
            } catch (e: Exception) {
                e.printStackTrace()
            }


            options.setOnClickListener {
                showPopupMenu(it, taskList)
            }


            try {
                val inputDate= inputDateFormat.parse(taskList.date)
                val outputDateString = inputDate?.let { dateFormat.format(it) }

                val items1 = outputDateString?.split(" ")?.toTypedArray()
                val inputsDay = items1?.get(0)
                val inputDd = items1?.get(1)
                val inputsMonth = items1?.get(2)

                day.text = inputsDay
                date.text = inputDd
                month.text = inputsMonth
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    private fun showPopupMenu(view: View, task: Tasks) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuDelete -> {
                    showDeleteConfirmationDialog(view.context, task)
                    true
                }
                R.id.menuUpdate -> {
                    listener.onUpdateClick(task)
                    true
                }
                R.id.menuComplete -> {
                    listener.onCompleteClick(task)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    private fun showDeleteConfirmationDialog(context: Context, task: Tasks) {
        AlertDialog.Builder(context)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { dialog, _ ->
                listener.onDeleteClick(task)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    interface HandleClickListener {
        fun onDeleteClick(tasks: Tasks)
        fun onUpdateClick(tasks: Tasks)
        fun onCompleteClick(tasks: Tasks)
    }



}