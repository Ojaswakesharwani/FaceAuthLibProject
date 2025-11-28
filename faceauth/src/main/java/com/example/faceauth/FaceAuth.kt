package com.example.faceauth

import android.content.Context
import android.content.Intent
import com.example.faceauth.ui.DocumentPickerActivity
import com.example.faceauth.ui.SelfieCameraActivity
import com.example.faceauth.utils.AuthenticationMode

object FaceAuth {
    private lateinit var appContext: Context
    private var mode: AuthenticationMode = AuthenticationMode.IDENTITY


    fun init(context: Context, verificationMode: AuthenticationMode = AuthenticationMode.IDENTITY) {
        appContext = context.applicationContext
        mode = verificationMode
    }

    fun getMode() = mode

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