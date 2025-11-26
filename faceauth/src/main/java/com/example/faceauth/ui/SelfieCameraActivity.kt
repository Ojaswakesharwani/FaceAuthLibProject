package com.example.faceauth.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.faceauth.databinding.ActivitySelfieCameraBinding
import com.example.faceauth.utils.ImageUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class SelfieCameraActivity : AppCompatActivity() {
    private val cameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startCamera()
        else Toast.makeText(this, "Camera Permission Required", Toast.LENGTH_SHORT).show()
    }
    private lateinit var binding: ActivitySelfieCameraBinding
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelfieCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraPermission.launch(android.Manifest.permission.CAMERA)

       // startCamera()  // 🔥 Start Camera on launch

        binding.captureBtn.setOnClickListener { captureSelfie() } // Manual capture for now
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            imageCapture = ImageCapture.Builder().build()

            val selector = CameraSelector.DEFAULT_FRONT_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, selector, preview, imageCapture)

        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureSelfie() {
        val output = ImageCapture.OutputFileOptions.Builder(
            createTempFile("selfie_", ".jpg", cacheDir)
        ).build()

        imageCapture.takePicture(
            output,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    val bitmap = ImageUtils.uriToBitmap(this@SelfieCameraActivity, result.savedUri!!)
                    if (bitmap != null) detectFace(bitmap)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@SelfieCameraActivity, "Capture Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    /** 🔥 Extract face from selfie */
    private fun detectFace(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        FaceDetection.getClient(FaceDetectorOptions.Builder().build()).process(image)
            .addOnSuccessListener { faces ->
                if (faces.isNotEmpty()) {
                    val box = faces.first().boundingBox
                    val face = Bitmap.createBitmap(bitmap, box.left, box.top, box.width(), box.height())

                    ImageUtils.storeSelfieFace(this, face)    // ⬅ correct storage

                    Toast.makeText(this, "Selfie Saved ✔", Toast.LENGTH_SHORT).show()
                    finish() // Continue to authentication screen (next milestone)
                }
            }
    }
}