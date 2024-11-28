package com.dicoding.acnescan.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.exp

class ImageClassifierHelper(context: Context, modelPath: String) {

    private val interpreter: Interpreter
    private val inputImageSize = 150
    private val numClasses = 5

    init {
        // Load the TFLite model
        val model = FileUtil.loadMappedFile(context, modelPath)
        interpreter = Interpreter(model)
    }

    /**
     * Preprocesses a Bitmap to the required input format.
     */
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        // Resize the bitmap to the expected input size
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputImageSize, inputImageSize, true)

        // Save the scaledBitmap locally for debugging
//        saveBitmapToLocal(scaledBitmap, "scaled_bitmap.png")

        // Log the input image size for debugging
        Log.d("ImageClassifierHelper", "Input image size: ${scaledBitmap.width}x${scaledBitmap.height}")

        // Allocate a ByteBuffer for the image data
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputImageSize * inputImageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder()) // Set the byte order

        // Convert bitmap pixels to a ByteBuffer in RGB format and normalize to [0, 1]
        for (y in 0 until inputImageSize) {
            for (x in 0 until inputImageSize) {
                val pixel = scaledBitmap.getPixel(x, y)
                byteBuffer.putFloat(Color.red(pixel) / 255.0f) // Normalize Red
                byteBuffer.putFloat(Color.green(pixel) / 255.0f) // Normalize Green
                byteBuffer.putFloat(Color.blue(pixel) / 255.0f) // Normalize Blue
            }
        }
        return byteBuffer
    }

    /**
     * Apply softmax to the raw logits.
     */
    private fun applySoftmax(logits: FloatArray): FloatArray {
        // To improve clarity, I'll use a simple loop instead of `map`
        val expValues = FloatArray(logits.size)
        var sumExpValues = 0f

        // Calculate exp(logits) for each value
        for (i in logits.indices) {
            expValues[i] = exp(logits[i].toDouble()).toFloat()
            sumExpValues += expValues[i]
        }

        // Normalize to get probabilities
        for (i in expValues.indices) {
            expValues[i] /= sumExpValues
        }

        return expValues
    }


    /**
     * Runs inference on the input Bitmap and returns predictions.
     */
    fun classifyImage(bitmap: Bitmap): Pair<String, Float> {
        val inputBuffer = preprocessImage(bitmap)

        // Allocate output buffer
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, numClasses), DataType.FLOAT32)

        // Run the model
        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        // Get the raw output array (logits)
        val outputArray = outputBuffer.floatArray

        // Tambahkan log untuk melihat output mentah
        Log.d("ModelOutput", "Raw Output: ${outputArray.joinToString()}")

        // Apply Softmax to logits
        val probabilities = applySoftmax(outputArray)

        // Debugging: Log the probabilities after Softmax
        Log.d("ImageClassifierHelper", "Softmax Probabilities: ${probabilities.joinToString()}")

        // Get the class with the highest probability
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val predictedClass = if (maxIndex >= 0) classLabels[maxIndex] else "Unknown"
        val confidence = probabilities[maxIndex]

        return Pair(predictedClass, confidence)
    }

    fun close() {
        interpreter.close()
    }

    // Define the class labels in the correct order as per your model
    private val classLabels = listOf("Blackheads", "Cyst", "Papules", "Pustules", "Whiteheads")
}