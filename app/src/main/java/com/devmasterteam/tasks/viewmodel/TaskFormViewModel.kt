package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    private val _priorities = MutableLiveData<List<PriorityModel>>()
    val priorities: LiveData<List<PriorityModel>> = _priorities

    private val _taskStatus = MutableLiveData<ValidationModel>()
    val taskStatus: LiveData<ValidationModel> = _taskStatus

    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task


    fun loadPriorities() {
        _priorities.value = priorityRepository.listFromDatabase()
    }

    fun createTask(task: TaskModel) {
        taskRepository.createTask(task, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                _taskStatus.value = ValidationModel(status = result)

            }

            override fun onFailure(result: String) {
                _taskStatus.value = ValidationModel(message = result)

            }

        })
    }

    fun updateTask(task: TaskModel) {
        taskRepository.updateTask(task, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                _taskStatus.value = ValidationModel(status = result)
            }

            override fun onFailure(result: String) {
                _taskStatus.value = ValidationModel(message = result)
            }
        })
    }

    fun loadTask(id: Int) {
        taskRepository.getById(id, object : APIListener<TaskModel> {
            override fun onSuccess(result: TaskModel) {
                _task.value = result
            }

            override fun onFailure(result: String) {
                _taskStatus.value = ValidationModel(message = result)
            }

        })
    }


}