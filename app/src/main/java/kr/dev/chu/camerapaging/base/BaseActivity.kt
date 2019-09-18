package kr.dev.chu.camerapaging.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kr.dev.chu.camerapaging.etc.changeStatusBar

abstract class BaseActivity<T : ViewDataBinding>: AppCompatActivity() {

    abstract val TAG: String
    abstract val getContentView: Int

    private var binding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView)

        binding = DataBindingUtil.setContentView(this, getContentView)

        title = ""
        changeStatusBar(android.R.color.black)

        initView()
        initFunc()
    }

    abstract fun initView()
    open fun initFunc() {}

    fun getBinding(): T = binding ?: DataBindingUtil.setContentView(this, getContentView)

    fun checkActive(): Boolean = !isFinishing
}