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

    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> = _taskSave

    fun loadPriorities() {
        _priorities.value = priorityRepository.listFromDatabase()
    }

    fun createTask(task: TaskModel) {
        taskRepository.createTask(task, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                _taskSave.value = ValidationModel(status = result)

            }

            override fun onFailure(result: String) {
                _taskSave.value = ValidationModel(message = result)

            }

        })
    }
}