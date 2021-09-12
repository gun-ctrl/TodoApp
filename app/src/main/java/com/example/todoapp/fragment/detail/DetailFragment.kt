package com.example.todoapp.fragment.detail

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Notification
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.model.*
import com.example.todoapp.data.model.Date
import com.example.todoapp.databinding.FragmentDetailBinding
import com.example.todoapp.dp2x
import com.example.todoapp.fragment.main.MainViewModel
import java.util.*


class DetailFragment : Fragment(){
    private lateinit var binding:FragmentDetailBinding
    //接受传递过来的参数
    private val mCurrentTodoArgs:DetailFragmentArgs by navArgs()
    //将Args解析出来
    private var mCurrentTodo:Todo ?= null
    private val mainViewModel: MainViewModel by viewModels()
    private val detailViewModel:DetailViewModel by viewModels()
    private var mTagData:TagData ? = null
    private var lastSelectedTagView:View ?  =null
    private var mPriority = Priority.LOW
    private var mDate = Date(
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        mCurrentTodo = mCurrentTodoArgs.currentTodo
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //优先级按钮的点击事件
        initPriorityEvent()

        //日期的点击事件
        initDateEvent()
        //顶部菜单点击事件
        initMenuEvent()

        binding.backBtn.setOnClickListener { goBack() }

        //初始化显示的数据
        initData()

        //save还是update
        if (mCurrentTodo != null){
            binding.saveBtn.text = "Update"
        }else{
            binding.deleteBtn.visibility = View.INVISIBLE
        }
        initAddTagEvent()

        //观察tag标签属性
        detailViewModel.tagList.observe(viewLifecycleOwner){
            //清除容器之前的子视图
            binding.tagContainer.removeAllViews()
            //改变tag的显示
            it.forEach {tagData ->
                //创建一个textView显示Tag
                TextView(requireContext()).apply {
                    text = tagData.title
                    setBackgroundColor(Color.parseColor(tagData.bgColor))
                    setTextColor(Color.WHITE)
                    setPadding(dp2x(requireContext(),15),
                        dp2x(requireContext(),10),
                        dp2x(requireContext(),15),
                        dp2x(requireContext(),10))
                    setOnClickListener {
                        if (lastSelectedTagView!=null){
                            lastSelectedTagView?.setBackgroundColor(Color.parseColor(mTagData?.bgColor))
                        }
                        mTagData = tagData
                        lastSelectedTagView = this
                        this.setBackgroundColor(requireActivity().getColor(R.color.main_purple))
                    }
                    binding.tagContainer.addView(this)
                }

            }
        }
    }
    private fun initAddTagEvent(){
        binding.addTagBtn.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_addTagDialogFragment)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun initData(){
        mCurrentTodo?.let {
            //将数据显示到界面上
            binding.titleEditText.setText(it.title)
            setPriority(it.priority)
            binding.descriptionView.setText(it.description)
            binding.dateBtn.text = "${it.date.year}.${it.date.month+1}.${it.date.day}"
        }
    }

    private fun setPriority(priority: Priority){
        mPriority = priority
        binding.priorityhighBtn.setTextColor(requireContext().getColor(R.color.white))
        binding.prioritylowBtn.setTextColor(requireContext().getColor(R.color.white))
        binding.prioritymiddelBtn.setTextColor(requireContext().getColor(R.color.white))
        when(priority){
            Priority.LOW -> binding.prioritylowBtn.setTextColor(requireContext().getColor(R.color.main_purple))
            Priority.MIDDLE -> binding.prioritymiddelBtn.setTextColor(requireContext().getColor(R.color.main_purple))
            Priority.HIGH -> binding.priorityhighBtn.setTextColor(requireContext().getColor(R.color.main_purple))
        }
    }

    private fun goBack(){
        //navigation中已经设置从mainfragment -> detailfragment的动画，onBackPressed直接模拟返回
        //缺点：无法传递数据
        requireActivity().onBackPressed()
    }

    private fun initMenuEvent(){
        binding.menuBtn.setOnClickListener {
            binding.optionBtnContainer.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(
                binding.optionBtnContainer,"rotationX",-90f,0f).apply {
                    duration = 300
                    interpolator = BounceInterpolator()
                    start()
            }

        }
        binding.saveBtn.setOnClickListener {
            if (checkInputValid()) {
                if (mCurrentTodo==null) {
                    //创建Todo对象
                    val TodoData = Todo(
                        0,
                        binding.titleEditText.text.toString(),
                        binding.descriptionView.text.toString(),
                        mPriority,
                        mDate,
                        Tag(mTagData!!.title, mTagData!!.bgColor)
                    )
                    //插入数据
                    mainViewModel.insertTodoData(TodoData)


                } else {
                    mCurrentTodo?.title = binding.titleEditText.text.toString()
                    mCurrentTodo?.description = binding.descriptionView.text.toString()
                    mCurrentTodo?.priority = mPriority
                    mCurrentTodo?.date = mDate
                    if (mTagData!=null){
                        mCurrentTodo?.tag = Tag(mTagData!!.title, mTagData!!.bgColor)
                    }
                    //更新数据
                    mainViewModel.updateTodoData(mCurrentTodo!!)
                }
                optionBtnHideAnim()
                goBack()
            }else{
                Toast.makeText(requireContext(), "有必填项未填写", Toast.LENGTH_LONG).show()
            }

        }

        binding.deleteBtn.setOnClickListener {
          mainViewModel.deleteTodoData(mCurrentTodo!!)
            optionBtnHideAnim()
           goBack()
        }

    }
    private fun checkInputValid():Boolean{
        return binding.titleEditText.text!!.isNotEmpty() &&
                binding.descriptionView.text.isNotEmpty() &&
                mTagData!=null
    }
    private fun optionBtnHideAnim(){
        binding.optionBtnContainer
            .animate()
            .rotationX(-90f)
            .setDuration(300)
                //使动画结束才消失
            .setListener(object :Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionBtnContainer.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
    }

    private fun initPriorityEvent(){
        binding.priorityhighBtn.setOnClickListener {
            setPriority(Priority.HIGH)
        }
        binding.prioritymiddelBtn.setOnClickListener {
            setPriority(Priority.MIDDLE)
        }
        binding.prioritylowBtn.setOnClickListener {
            setPriority(Priority.LOW)
        }
    }

    private fun initDateEvent() {
        binding.dateBtn.setOnClickListener {
            //DatePickerDialog弹出日期选项的控件
            DatePickerDialog(
                requireContext(), object : DatePickerDialog.OnDateSetListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDateSet(
                        p0: DatePicker?,
                        year: Int,
                        month: Int,
                        dayofMonth: Int
                    ) {

                        mDate.year = year
                        mDate.month = month
                        mDate.day = dayofMonth

                        //将日期显示到dateBtn上
                        binding.dateBtn.text = "$year-${month+1}-$dayofMonth"
                    }
                },
                mDate.year, mDate.month, mDate.day
            ).show()
        }


    }


}