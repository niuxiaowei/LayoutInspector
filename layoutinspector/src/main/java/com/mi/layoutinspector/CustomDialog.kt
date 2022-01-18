package com.mi.layoutinspector

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.layoutinspector_dialog_custom.*


/**
 * create by niuxiaowei
 * date : 22-1-13
 **/
class CustomDialog(context: Context, private val okClickListener: IOkClickListener, private val titleStr: String, private val editHint: String) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layoutinspector_dialog_custom)
        title.text = titleStr
        ok.setOnClickListener {
            okClickListener.onOkClick(edit.text.toString())
            dismiss()
        }
        edit.apply {
            hint = editHint
        }
    }

    interface IOkClickListener {
        fun onOkClick(editMsg: String)
    }

}