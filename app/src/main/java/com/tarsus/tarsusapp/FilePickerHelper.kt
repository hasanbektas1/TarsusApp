package com.tarsus.tarsusapp

import android.net.Uri
import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Cihazdaki Json dosyası içerigini okuma
class FileReaderHelper(private val filePath: String) {

    companion object {
        private const val TAG = "JSONFileReader"
    }

    var videoUri by mutableStateOf<Uri?>(null)
        private set

    fun readVideoPathFromJson(): Boolean {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                Log.e(TAG, "File does not exist: $filePath")
                return false
            }

            FileInputStream(file).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                    val jsonString = stringBuilder.toString()
                    val jsonElement = Json.parseToJsonElement(jsonString)
                    Log.d(TAG, "JSON Content: $jsonString")

                    if (jsonElement is JsonObject) {
                        val videosArray = jsonElement["videos"]?.jsonArray
                        if (videosArray != null && videosArray.isNotEmpty()) {
                            val videoPath = videosArray[0].jsonPrimitive.content
                            videoUri = Uri.parse(videoPath)
                            Log.d(TAG, "Video path found: $videoPath")
                            return true
                        } else {
                            Log.e(TAG, "No valid video path found in JSON")
                            return false
                        }
                    } else {
                        Log.e(TAG, "Invalid JSON format")
                        return false
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading JSON file", e)
            return false
        }
    }
}
