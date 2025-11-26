package com.example.faceauth.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    private const val AADHAAR_FACE = "aadhaar_face.jpg"
    private const val SELFIE_FACE = "selfie_face.jpg"

    /* -----------------------------------------------------
        URI → Bitmap Converter
    ------------------------------------------------------*/
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri).use { input ->
                BitmapFactory.decodeStream(input)
            }
        } catch (e: Exception) {
            Log.e("ImageUtils", "URI to Bitmap failed → $e")
            null
        }
    }

    /* -----------------------------------------------------
        SAVE BITMAP to Internal Storage
    ------------------------------------------------------*/
    private fun storeBitmap(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            val byteStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, byteStream)

            FileOutputStream(file).use { fos ->
                fos.write(byteStream.toByteArray())
            }

            Log.i("ImageUtils", "Saved → $fileName")
            true
        } catch (e: Exception) {
            Log.e("ImageUtils", "Saving failed → $e")
            false
        }
    }

    /* -----------------------------------------------------
        PUBLIC SAVE FUNCTIONS
    ------------------------------------------------------*/
    fun storeAadhaarFace(context: Context, bitmap: Bitmap) =
        storeBitmap(context, bitmap, AADHAAR_FACE)

    fun storeSelfieFace(context: Context, bitmap: Bitmap) =
        storeBitmap(context, bitmap, SELFIE_FACE)

    /* -----------------------------------------------------
        LOAD STORED FACES
    ------------------------------------------------------*/
    private fun loadBitmap(context: Context, fileName: String): Bitmap? {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return null
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            Log.e("ImageUtils", "Loading failed → $e")
            null
        }
    }

    fun getAadhaarFace(context: Context): Bitmap? = loadBitmap(context, AADHAAR_FACE)
    fun getSelfieFace(context: Context): Bitmap? = loadBitmap(context, SELFIE_FACE)
}
