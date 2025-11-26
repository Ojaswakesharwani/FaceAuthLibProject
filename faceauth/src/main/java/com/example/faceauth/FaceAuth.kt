package com.example.faceauth

import android.content.Context
import android.content.Intent
import com.example.faceauth.ui.DocumentPickerActivity
import com.example.faceauth.ui.SelfieCameraActivity

object FaceAuth {
    private lateinit var appContext: Context



    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun pickAadhaarImage() {
        val intent = Intent(appContext, DocumentPickerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }

    fun startSelfieAuth() {
        val intent = Intent(appContext, SelfieCameraActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }
}