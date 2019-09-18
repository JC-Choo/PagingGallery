package kr.dev.chu.camerapaging.etc

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.actionBarHide() {
    supportActionBar?.hide()
}

fun Activity.changeStatusBar(@ColorRes color: Int) {
//    val window = window
//    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//    window.statusBarColor = this.getColorById(color)

    val ctx = this

    window.run {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = ctx.getColorById(color)
    }
}

fun Context.getColorById(@ColorRes res: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) getColor(res) else resources.getColor(res)

fun Context.showToast(message: String) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }

fun Context.showToast(@StringRes messageRes: Int) { Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show() }