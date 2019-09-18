package kr.dev.chu.camerapaging.view

import android.content.Context
import android.net.Uri
import android.util.Log
import kr.dev.chu.camerapaging.R
import kr.dev.chu.camerapaging.base.BaseDialog
import kr.dev.chu.camerapaging.databinding.DialogImageBinding

class ImageDialog(
    context: Context,
    private val uri: Uri
) : BaseDialog<DialogImageBinding>(context) {

    override val TAG: String
        get() = ImageDialog::class.java.simpleName

    override val layoutRes: Int
        get() = R.layout.dialog_image

    override fun initView() {
        Log.i(TAG, "initView")

        getBinding().dialog = this
        getBinding().imageIv.setImageURI(uri)
    }
}