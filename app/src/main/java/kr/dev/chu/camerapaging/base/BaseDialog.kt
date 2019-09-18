package kr.dev.chu.camerapaging.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDialog<T: ViewDataBinding>(context: Context) : Dialog(context) {

    private var viewBinding: T? = null
    abstract val TAG: String

    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, null, false)
        setContentView(viewBinding!!.root)

        initView()
        onClick()
    }

    abstract fun initView()
    open fun onClick() {}

    fun getBinding(): T = viewBinding!!
}