package com.devmasterteam.tasks.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _fail = MutableLiveData<String>()
    val fail: LiveData<String> = _fail

    private val _priority = MutableLiveData<List<PriorityModel>>()
    val priority: LiveData<List<PriorityModel>> = _priority

    fun listTasks() {
        taskRepository.getAll(object : APIListener<List<TaskModel>> {
            override fun onSuccess(result: List<TaskModel>) {
                _tasks.value = result
            }

            override fun onFailure(result: String) {
                _fail.value = result
            }
        })
    }

    fun listPriority() {
        _priority.value = priorityRepository.listFromDatabase()
    }

    fun deleteTask(id: Int) {
        taskRepository.deleteTask(id, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                val s = ""
            }

            override fun onFailure(result: String) {
                val s = ""
            }

        })
    }

}