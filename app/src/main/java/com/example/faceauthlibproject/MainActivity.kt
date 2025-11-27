package com.example.faceauthlibproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.faceauth.FaceAuth
import com.example.faceauth.ui.AuthResultActivity
import com.example.faceauth.utils.ImageUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        FaceAuth.init(this)
        FaceAuth.pickAadhaarImage()

        val savedAadhaarFace = ImageUtils.getAadhaarFace(this)
        val savedSelfieFace = ImageUtils.getSelfieFace(this)

        if (savedAadhaarFace != null && savedSelfieFace == null) {
            FaceAuth.startSelfieAuth()     // After Aadhaar extraction → capture selfie
        }

        if (savedAadhaarFace != null && savedSelfieFace != null) {
            startActivity(Intent(this, AuthResultActivity::class.java))  // Proceed to verification
        }

    }
}