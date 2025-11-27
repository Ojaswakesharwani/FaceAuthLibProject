package com.example.faceauth.utils

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

object FaceMatcher {

    private var interpreter: Interpreter? = null

    fun loadModel(context: Context) {
        if (interpreter != null) return

        val model = context.assets.open("facenet.tflite").readBytes()
        val buffer = ByteBuffer.allocateDirect(model.size).apply {
            order(ByteOrder.nativeOrder())
            put(model)
            rewind()
        }
        interpreter = Interpreter(buffer)
    }


    fun getEmbedding(bitmap: Bitmap): FloatArray {
        val resized = Bitmap.createScaledBitmap(bitmap, 160, 160, true)
        val whitened = prewhiten(resized)
        val input = toByteBuffer(whitened)

        val output = Array(1) { FloatArray(128) }
        interpreter?.run(input, output)

        return l2Normalize(output[0])          // 🔥 critical to compare
    }


    private fun prewhiten(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val pixels = IntArray(w*h)
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h)

        val mean = pixels.map { (it shr 16 and 255) + (it shr 8 and 255) + (it and 255) }
            .average().toFloat() / 3f

        val std = sqrt(
            pixels.map {
                val r = (it shr 16 and 255) - mean
                val g = (it shr 8 and 255) - mean
                val b = (it and 255) - mean
                ((r*r + g*g + b*b) / 3f).toDouble()
            }.average()
        ).toFloat()

        val stdAdj = max(std, 1f / sqrt(pixels.size.toFloat()))

        val newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        for (i in pixels.indices) {
            val r = (((pixels[i] shr 16) and 255) - mean) / stdAdj
            val g = (((pixels[i] shr  8) and 255) - mean) / stdAdj
            val b = (((pixels[i])        and 255) - mean) / stdAdj

            pixels[i] = (255 shl 24) or
                    ((r * 128 + 128).toInt().coerceIn(0,255) shl 16) or
                    ((g * 128 + 128).toInt().coerceIn(0,255) shl 8) or
                    ( (b * 128 + 128).toInt().coerceIn(0,255) )
        }

        newBitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return newBitmap
    }


    private fun l2Normalize(arr: FloatArray): FloatArray {
        val norm = sqrt(arr.sumOf { (it * it).toDouble() }).toFloat()
        return arr.map { it / norm }.toFloatArray()
    }


    private fun toByteBuffer(bitmap: Bitmap): ByteBuffer {
        val data = ByteBuffer.allocateDirect(160*160*3*4).apply {
            order(ByteOrder.nativeOrder())
        }

        for (y in 0 until 160) for (x in 0 until 160) {
            val p = bitmap.getPixel(x, y)
            data.putFloat(((p shr 16 and 255) - 127.5f) / 128f)
            data.putFloat(((p shr 8 and 255) - 127.5f) / 128f)
            data.putFloat(((p and 255) - 127.5f) / 128f)
        }
        return data
    }


    fun compare(e1: FloatArray, e2: FloatArray): Float {
        var sum = 0f
        for (i in e1.indices) sum += (e1[i] - e2[i]).pow(2)
        return sqrt(sum)           // euclidean distance
    }
}
