package com.wonddak.coinaverage

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wonddak.coinaverage.databinding.DialogNewGameBinding

class Dialog(
    context: Context,
    val activity: MainActivity
) {
    val dialog = Dialog(context)
    val params = dialog.window!!.attributes

    fun newGameStart() {
        val context: Context = this.dialog.context
        val binding = DialogNewGameBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }
}