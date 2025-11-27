package com.example.faceauth.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.faceauth.R
import com.example.faceauth.utils.FaceMatcher
import com.example.faceauth.utils.ImageUtils

class AuthResultActivity : AppCompatActivity() {

    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_result)

        text = findViewById(R.id.message)

        val aadhaar = ImageUtils.getAadhaarFace(this)
        val selfie  = ImageUtils.getSelfieFace(this)

        Log.d("AUTH_FLOW", "Aadhaar = ${aadhaar != null}, Selfie = ${selfie != null}")

        if (aadhaar == null || selfie == null) {
            text.text = "⚠ Missing Aadhaar/Selfie Images"
            Toast.makeText(this, "Capture Again!", Toast.LENGTH_LONG).show()
            return
        }

        try {
            FaceMatcher.loadModel(this)
            Log.d("AUTH_FLOW", "Model Loaded Successfully")

            // generate embeddings
            val emb1 = FaceMatcher.getEmbedding(aadhaar)
            val emb2 = FaceMatcher.getEmbedding(selfie)

            // compare
            val distance = FaceMatcher.compare(emb1, emb2)
            Log.e("AUTH_FLOW_RESULT", "DISTANCE => $distance")

            if (distance < 1.15f) {
                text.text = "✔ MATCH — USER VERIFIED"
                Toast.makeText(this, "✔ MATCH — VERIFIED", Toast.LENGTH_LONG).show()
            } else {
                text.text = "❌ FACE MISMATCH — AUTH FAILED"
                Toast.makeText(this, "❌ AUTH FAILED", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Log.e("AUTH_FLOW_ERROR", "${e.message}")
            Toast.makeText(this, "❌ Processing Failed", Toast.LENGTH_LONG).show()
            text.text = "❌ ERROR — TRY AGAIN"
        }
    }
}
