package com.example.managerapp.utils

import android.app.ProgressDialog
import android.content.Context

class Dialog {

    fun showLoadingDialog(context : Context): ProgressDialog {
        val mProgressDialog = ProgressDialog(context)
        mProgressDialog.setTitle("Please wait...")
        mProgressDialog.setMessage("Loading")
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
        return mProgressDialog
    }
}