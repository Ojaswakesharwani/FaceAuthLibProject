package com.example.faceauthlibproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.faceauth.FaceAuth
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

        if (savedSelfieFace != null){
            Toast.makeText(this, "Selfie Face Exists", Toast.LENGTH_SHORT).show()

        }

        Log.d("TEST", "Aadhaar Face Exists = ${savedAadhaarFace != null}")
        Log.d("TEST", "Selfie Face Exists = ${savedSelfieFace != null}")

    }
}