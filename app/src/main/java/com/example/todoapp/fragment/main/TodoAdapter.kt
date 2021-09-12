package com.example.todoapp.fragment.main

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Priority
import com.example.todoapp.data.model.Todo

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class TodoAdapter:RecyclerView.Adapter<TodoAdapter.MyViewHolder>() {
    private var dataList: List<Todo> = emptyList()

    class MyViewHolder(view:View) : RecyclerView.ViewHolder(view) {

        private  var dateTextView:TextView = itemView.findViewById(R.id.date)
        private  var priorityImageView: ImageView = itemView.findViewById(R.id.priority)
        private  var titleTextView: TextView = itemView.findViewById(R.id.todo_title)
        private  var timeTextView: TextView = itemView.findViewById(R.id.time)
        private  var tagTextView:TextView = itemView.findViewById(R.id.tag)
        //使用静态方法封装创建MyViewHolder的方法
        companion object {
            fun from(parent:ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.todo_item_layout,
                parent,
                false)
                return MyViewHolder(view)
            }
        }
        //绑定数据
        @SuppressLint("SetTextI18n")
        fun bindData(todo:Todo){
            //标题
            titleTextView.text = todo.title
            //标签
            tagTextView.text = todo.tag.text
            tagTextView.backgroundTintList = ColorStateList
                    .valueOf(Color.parseColor(todo.tag.bgColor))
            //优先级
            when(todo.priority){
                Priority.HIGH ->{priorityImageView.setImageResource(R.drawable.red_plate)}
                Priority.MIDDLE ->{priorityImageView.setImageResource(R.drawable.green_plate)}
                Priority.LOW ->{priorityImageView.setImageResource(R.drawable.yellow_plate)}
            }
            //时间
            dateTextView.text = "${todo.date.month}.${todo.date.day}"
            timeTextView.text = "${todo.date.year}-${todo.date.month+1}-${todo.date.day}"

           itemView.setOnClickListener {
               val action = MainFragmentDirections.actionMainFragmentToDetailFragment(todo)
               it.findNavController().navigate(action)
               //在Activity以及Fragment中可以直接通过findNavController找到NavController，
               // 其他地方则可以通过视图调用findNavController方法
           }

        }
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder.from(parent)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindData(dataList[position])
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
       fun setDatas(newData:List<Todo>){
        val diffResult =  DiffUtil.calculateDiff(TodoDiffUtil(newData,dataList))
        dataList = newData
        diffResult.dispatchUpdatesTo(this)
     }
}
