package com.example.todoapp.fragment.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Priority
import com.example.todoapp.data.model.Todo
import com.example.todoapp.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var binding:FragmentMainBinding
    private val mViewModel: MainViewModel by viewModels()
    private val mAdapter:TodoAdapter by lazy { TodoAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentMainBinding.inflate(inflater)

        return binding.root
    }

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        mViewModel.todoDataList.observe(viewLifecycleOwner, Observer {

            //检查是否有数据
            checkEmptyStatus(it)

            //第一条数据显示在头部
            if (it.isNotEmpty()){
                initTopTodo(it[0])
            }
            if (it.size > 1){
                //把除第一条的数据添加到后面
                //包含fromIndex 不包含ToIndex
                mAdapter.setDatas(it.subList(1,it.size))
            }else{
                mAdapter.setDatas(emptyList())
            }

            //更新Adapter的数据
            mAdapter.setDatas(it)

        })
        binding.addBtn.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(null)
            findNavController().navigate(action)
        }
        binding.TopTaskContainer.setOnClickListener {
            //获取第一个数据
            val data = mViewModel.todoDataList.value!![0]
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(data)
            findNavController().navigate(action)
        }
        binding.deleteAllBtn.setOnClickListener {
            //删除所有todo
            mViewModel.deleteAllTodoDatas()
            Toast.makeText(requireContext(),"DeleteAll Finished!",Toast.LENGTH_LONG)
        }
    }

    //初始化头部信息
    @SuppressLint("SetTextI18n")
    private fun initTopTodo(todo: Todo){
        binding.topTitle.text = todo.title
        binding.topTime.text  = "${todo.date.year}.${todo.date.month+1}.${todo.date.day}"
        binding.topDescription.text = todo.description
        binding.topTag.text = todo.tag.text
        when(todo.priority){
            Priority.HIGH ->binding.topPriority.setImageResource(R.drawable.red_ball)
            Priority.MIDDLE ->binding.topPriority.setImageResource(R.drawable.green)
            Priority.LOW ->binding.topPriority.setImageResource(R.drawable.yellow_ball)
        }
        binding.topTag.background.setTint(Color.parseColor(todo.tag.bgColor))


    }
    //初始化recyclerview
    private fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
        RecyclerView.VERTICAL,
        false)
        binding.recyclerView.adapter = mAdapter

        //滑动删除
        swipeToDelete()
    }

    //检查是否显示无数据状态
    private fun checkEmptyStatus(dataList:List<Todo>){
        if (dataList.isEmpty()){
            binding.ivEmptyFace.visibility = View.VISIBLE
            binding.ivEmpty.visibility = View.VISIBLE
            //隐藏头部视图
            binding.TopTaskContainer.visibility = View.INVISIBLE
        }else{
            binding.ivEmptyFace.visibility = View.INVISIBLE
            binding.ivEmpty.visibility = View.INVISIBLE
            //显示头部视图
            binding.TopTaskContainer.visibility = View.VISIBLE
        }
    }

    /**
     * 滑动删除
     */
    private fun swipeToDelete(){
       val touchHelper =  ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return  false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //获取滑动的位置
                val index = viewHolder.adapterPosition
                //将数据从数据源中删除
                val data = mViewModel.todoDataList.value!![index]
                mViewModel.deleteTodoData(data)
                //弹出删除提示信息
                Toast.makeText(requireContext(),"Delete Finished!",Toast.LENGTH_LONG)
            }

        })
        //将滑动删除事件绑定到recyclerview
        touchHelper.attachToRecyclerView(binding.recyclerView)
    }

}