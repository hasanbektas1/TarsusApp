package com.tarsus.tarsusapp

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class FileReaderHelper(private val filePath: String) {

    companion object {
        private const val TAG = "JSONFileReader"
    }

    fun readVideoPathsFromJson(): Map<String, String>? {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                return null
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

                    if (jsonElement is JsonObject) {
                        val videosObject = jsonElement["videos"]?.jsonObject
                        return videosObject?.mapValues { it.value.jsonPrimitive.content }
                    } else {
                        return null
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }
    }
}