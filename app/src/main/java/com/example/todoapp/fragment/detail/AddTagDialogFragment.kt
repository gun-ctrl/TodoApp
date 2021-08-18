package com.example.todoapp.fragment.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.todoapp.data.model.TagData
import com.example.todoapp.databinding.AddTagDialogBinding

/**
 *@Description
 *@Author PC
 *@QQ 1578684787
 */
class AddTagDialogFragment:DialogFragment() {
    private lateinit var binding:AddTagDialogBinding
    private var bgColor = "#ff8B7C"
    private val mViewModel:DetailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTagDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.color1.setOnClickListener { colorViewSelected(it)}
        binding.color2.setOnClickListener {colorViewSelected(it) }
        binding.color3.setOnClickListener {colorViewSelected(it) }
        binding.color4.setOnClickListener {colorViewSelected(it) }
        //dialog和fragment的退出效果不同
        binding.cancelBtn.setOnClickListener { dismiss() }
        binding.okBtn.setOnClickListener {
           saveTag()
        }
    }
    @SuppressLint("ShowToast")
    private fun saveTag(){
        if (binding.tagEditTextName.text.isEmpty()){
            Toast.makeText(requireContext(),"标签名不能为空",Toast.LENGTH_LONG)
        }else{
            mViewModel.insertTag(TagData(0,binding.tagEditTextName.text.toString(),bgColor))
            dismiss()
        }
    }
    private fun colorViewSelected(view: View){
        //设置颜色按钮下方下滑线动画
        binding.underline
                .animate()
                .translationX(view.x - binding.color1.x)
                .setDuration(500)
                .start()
        bgColor = view.tag as String
    }
}