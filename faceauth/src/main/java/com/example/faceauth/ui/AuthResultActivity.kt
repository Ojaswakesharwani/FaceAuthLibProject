package com.example.faceauth.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.faceauth.FaceAuth
import com.example.faceauth.R
import com.example.faceauth.utils.AuthenticationMode
import com.example.faceauth.utils.FaceMatcher
import com.example.faceauth.utils.ImageUtils

class AuthResultActivity : AppCompatActivity() {

    private lateinit var message: TextView
    lateinit var subMsg : TextView
    lateinit var resultIcon : ImageView

    private val threshold: Float
        get() = when (FaceAuth.getMode()) {
            AuthenticationMode.DOCUMENT -> 1.10f      // Aadhaar / PAN validation
            AuthenticationMode.IDENTITY -> 0.90f      // Strong face match
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_result)

        message = findViewById(R.id.message)
        subMsg = findViewById(R.id.subMsg)
        resultIcon = findViewById(R.id.resultIcon)


        val aadhaar = ImageUtils.getAadhaarFace(this)
        val selfie  = ImageUtils.getSelfieFace(this)


        Log.d("AUTH_FLOW", "Aadhaar = ${aadhaar != null}, Selfie = ${selfie != null}")

        if (aadhaar == null || selfie == null) {
            message.text = "⚠ Required images missing!"
            subMsg.text = "Upload or capture again."
            resultIcon.setImageResource(R.drawable.ic_failed)
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

            if (distance < threshold) {
                resultIcon.setImageResource(R.drawable.ic_success)
                message.text = "✔ Verification Successful"
                subMsg.text = "Face matched with document."
            } else {
                resultIcon.setImageResource(R.drawable.ic_failed)
                message.text = "❌ Verification Failed"
                subMsg.text = "Face did not match. Please retry."
            }



        } catch (e: Exception) {
            Log.e("AUTH_FLOW_ERROR", "${e.message}")
            Toast.makeText(this, "❌ Processing Failed", Toast.LENGTH_LONG).show()
            message.text = "❌ ERROR — TRY AGAIN"
        }
    }
}
