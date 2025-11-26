package com.example.faceauth.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

object FaceExtractor {

    fun extractFaceFromDocument(context: Context, uri: Uri, onFaceExtracted: ((Bitmap) -> Unit)? = null) {
        val bitmap = ImageUtils.uriToBitmap(context, uri)
        if (bitmap == null) {
            Toast.makeText(context, "Image loading failed!", Toast.LENGTH_SHORT).show()
            return
        }

        val image = InputImage.fromBitmap(bitmap, 0)

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()

        val detector = FaceDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isEmpty()) {
                    Toast.makeText(context, "No face detected!", Toast.LENGTH_SHORT).show()
                    Log.e("FaceExtractor", "Faces list returned empty")
                    return@addOnSuccessListener
                }

                val face = faces.first()
                val box: Rect = face.boundingBox

                // Prevent crash if bounding box overflows
                val x = box.left.coerceAtLeast(0)
                val y = box.top.coerceAtLeast(0)
                val width = box.width().coerceAtMost(bitmap.width - x)
                val height = box.height().coerceAtMost(bitmap.height - y)

                val cropped = Bitmap.createBitmap(bitmap, x, y, width, height)

                if (ImageUtils.storeAadhaarFace(context, cropped)) {
                    Toast.makeText(context, "Face Extracted & Saved ✔", Toast.LENGTH_SHORT).show()
                }

                onFaceExtracted?.invoke(cropped)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Face extraction failed!", Toast.LENGTH_SHORT).show()
                Log.e("FaceExtractor", "Error: $it")
            }
    }
}