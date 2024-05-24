package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var priorityList: List<String>
    private var dueDateValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        // Layout
        setContentView(binding.root)

        // Load Data
        viewModel.loadPriorities()

        // Observer
        observe()

    }

    override fun onResume() {
        super.onResume()
        handleSpinner()

    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_date) {
            handleDate()
        } else if (v.id == R.id.button_save) {
            handleSave()
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        dueDateValue = dateFormat.format(calendar.time)

        binding.buttonDate.text = getString(R.string.data_limite_text, dueDateValue)

    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun observe() {
        viewModel.priorities.observe(this) { it ->
            priorityList = it.map { it.description }
            println(priorityList)
        }

        viewModel.taskSave.observe(this) {
            if (it.status) {
                Toast.makeText(this, getString(R.string.task_created), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSpinner() {
        val priorities = priorityList
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Selecione") + priorities
        )
        binding.spinnerPriority.adapter = adapter
    }

    private fun handleSave() {

        val task = TaskModel().apply {
            this.id = 0
            this.description = binding.editDescription.text.toString()
            this.priorityId = binding.spinnerPriority.selectedItemPosition
            this.complete = binding.checkComplete.isChecked
            this.dueDate = dueDateValue
        }
        viewModel.createTask(task)

    }


}