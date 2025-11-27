package com.example.faceauth.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.faceauth.R
import com.example.faceauth.databinding.ActivityDocumentPickerBinding
import com.example.faceauth.utils.FaceExtractor
import com.example.faceauth.utils.ImageUtils

class DocumentPickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentPickerBinding

    private val filePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = ImageUtils.uriToBitmap(this, it)

            if (bitmap != null) {
                binding.documentImage.setImageBitmap(bitmap)      // 🔥 preview image inside card
                binding.continueBtn.visibility = android.view.View.VISIBLE // 🔥 enable continue
            }

            FaceExtractor.extractFaceFromDocument(this, it) { face ->
                val aligned = Bitmap.createScaledBitmap(face, 160, 160, true)
                ImageUtils.storeAadhaarFace(this, aligned)   // ⬅ Aadhaar Face Stored Separately

                binding.documentImage.setImageBitmap(aligned)
                binding.continueBtn.visibility = android.view.View.VISIBLE
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.pickAadhaarBtn.setOnClickListener {
            filePicker.launch("image/*")
        }

        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this, SelfieCameraActivity::class.java))
        }
    }
}