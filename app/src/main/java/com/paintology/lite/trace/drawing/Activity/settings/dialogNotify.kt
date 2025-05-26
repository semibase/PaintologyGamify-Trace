package com.paintology.lite.trace.drawing.Activity.settings

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import com.paintology.lite.trace.drawing.databinding.DialogNotifyBinding

fun Activity.dialogNotify(title: String, action: () -> Unit) {
    try {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        val binding = DialogNotifyBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvdes.text = title
        binding.cancel.setOnClickListener {
            if (dialog.isShowing) {
                dialog.dismiss()
                action.invoke()
            }
        }

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)

        try {
            dialog.show()
        } catch (_: Exception) {
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}