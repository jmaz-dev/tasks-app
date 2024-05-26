package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.helper.Logout
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val security = SecurityPreferences(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _fail = MutableLiveData<ValidationModel>()
    val fail: LiveData<ValidationModel> = _fail

    private val _priority = MutableLiveData<List<PriorityModel>>()
    val priority: LiveData<List<PriorityModel>> = _priority

    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> = _delete

    private val _complete = MutableLiveData<Boolean>()
    val complete: LiveData<Boolean> = _complete

    private var filter = 0

    fun listTasks(taskFilter: Int) {
        filter = taskFilter

        val listener = object : APIListener<List<TaskModel>> {
            override fun onSuccess(result: List<TaskModel>) {
                _tasks.value = result
            }

            override fun onFailure(result: String) {
                if (result == TaskConstants.HTTP.AUTH_ERROR) {
                    logout()
                }
                _fail.value = ValidationModel(result, true)
            }
        }

        when (taskFilter) {
            TaskConstants.FILTER.ALL -> taskRepository.getAll(listener)

            TaskConstants.FILTER.NEXT -> taskRepository.getNext7Days(listener)

            else -> taskRepository.getOverdue(listener)
        }

    }

    fun listPriority() {
        _priority.value = priorityRepository.listFromDatabase()
    }

    fun deleteTask(id: Int) {
        taskRepository.deleteTask(id, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                listTasks(filter)
                _delete.value = result
            }

            override fun onFailure(result: String) {
                _fail.value = ValidationModel(result, true)

            }

        })
    }

    fun setCompleteTask(id: Int, isComplete: Boolean) {
        val listener = object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                listTasks(filter)
                _complete.value = result
            }

            override fun onFailure(result: String) {
                _fail.value = ValidationModel(result, true)

            }

        }
        if (isComplete) {
            taskRepository.undoTask(id, listener)

        } else {
            taskRepository.completeTask(id, listener)
        }
    }

    fun logout() {
        Logout.logout(getApplication())
    }


}