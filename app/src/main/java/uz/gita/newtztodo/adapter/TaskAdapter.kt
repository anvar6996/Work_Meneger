package uz.gita.newtztodo.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uz.gita.newtztodo.R
import uz.gita.newtztodo.base.data.TaskData
import uz.gita.newtztodo.ui.utils.getColor
import javax.inject.Inject

class TaskAdapter @Inject constructor(var list: List<TaskData>, var query: String) :
    RecyclerView.Adapter<TaskAdapter.Item_Task>() {
    private var clickItem: ((Int, TaskData) -> Unit)? = null
    private var onceClick: ((Int, TaskData) -> Unit)? = null

    inner class Item_Task(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.findViewById<ConstraintLayout>(R.id.more).setOnLongClickListener()
            {
                clickItem?.invoke(absoluteAdapterPosition, list[absoluteAdapterPosition])
                return@setOnLongClickListener true
            }
            itemView.findViewById<ConstraintLayout>(R.id.more).setOnClickListener {
                onceClick?.invoke(absoluteAdapterPosition, list[absoluteAdapterPosition])
            }
        }

        private var title: TextView = itemView.findViewById(R.id.textTitle)
        private var description: TextView = itemView.findViewById(R.id.textDescription)
        private var time: TextView = itemView.findViewById(R.id.timeShow)
        fun load() {
            val data = list[absoluteAdapterPosition]
            val spanSt = SpannableString(data.title)
            var foregroundSpan = ForegroundColorSpan(getColor(android.R.color.holo_red_dark))
            val startIndex = data.title.indexOf(query, 0, true)
            var lastIndex = startIndex + query.length
            spanSt.setSpan(
                foregroundSpan, startIndex, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            title.text = spanSt
            description.text = data.description
            time.text = data.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item_Task =
        Item_Task(LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false))


    override fun onBindViewHolder(holder: Item_Task, position: Int) {
        holder.load()
    }

    fun setClickItem(f: (Int, TaskData) -> Unit) {
        clickItem = f
    }

    fun setOneCkick(f: (Int, TaskData) -> Unit) {
        onceClick = f
    }

    override fun getItemCount(): Int = list.size
}