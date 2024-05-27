package com.devmasterteam.tasks.view.viewholder

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.RowTaskListBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import java.text.SimpleDateFormat
import java.util.Locale

class TaskViewHolder(private val bind: RowTaskListBinding, private val listener: TaskListener) :
    RecyclerView.ViewHolder(bind.root) {


    fun bindData(task: TaskModel, priority: List<PriorityModel>) {
        if (task.dueDate != "") {
            /*Transforma em Date depois formata*/
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(task.dueDate)
            val dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date!!)
            val priorityDescription = priority.map { it.description }[task.priorityId - 1]

            bind.textDescription.text = task.description
            bind.textPriority.text = priorityDescription
            bind.textDueDate.text = dueDate
            if (task.complete) {
                bind.imageTask.setImageResource(R.drawable.ic_done)
            } else {
                bind.imageTask.setImageResource(R.drawable.ic_todo)
            }
        }

        // Eventos
        bind.conatinerTask.setOnClickListener { listener.onListClick(task.id) }
        bind.imageTask.setOnClickListener {

            listener.onCompleteClick(task.id, task.complete)

        }

        bind.conatinerTask.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { _, _ ->
                    listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }
}