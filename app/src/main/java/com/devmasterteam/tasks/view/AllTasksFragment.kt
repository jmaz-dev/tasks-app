package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.FragmentAllTasksBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.view.adapter.TaskAdapter
import com.devmasterteam.tasks.viewmodel.TaskListViewModel

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskAdapter()
    private lateinit var priority: List<PriorityModel>
    private var taskFilter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        viewModel = ViewModelProvider(this)[TaskListViewModel::class.java]
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter

        taskFilter = requireArguments().getInt(TaskConstants.BUNDLE.TASKFILTER, 0)

        // Adapter Listener
        val listener = object : TaskListener {
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                viewModel.deleteTask(id)
            }

            override fun onCompleteClick(id: Int, complete: Boolean) {
                viewModel.setCompleteTask(id, complete)
            }

        }
        adapter.attachListener(listener)

        // Cria os observadores
        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.listPriority()
        viewModel.listTasks(taskFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        /*doList*/
        viewModel.priority.observe(viewLifecycleOwner, Observer {
            priority = it
        })
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            if (priority.isNotEmpty()) {
                adapter.upadateList(it, priority)
            }
        })
        /*onDelete*/
        viewModel.delete.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, R.string.task_removed, Toast.LENGTH_SHORT).show()
            }
        })
        /*onComplete*/
        viewModel.complete.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, R.string.task_updated, Toast.LENGTH_SHORT).show()
            }
        })
        /*onFail*/
        viewModel.fail.observe(viewLifecycleOwner, Observer {
            if (it.isNotBlank()) {
                if (it == TaskConstants.HTTP.AUTH_ERROR) {
                    startActivity(Intent(context, LoginActivity::class.java))
                }
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })


    }


}