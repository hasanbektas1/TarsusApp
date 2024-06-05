package com.tarsus.tarsusapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class FilePickerHelper(private val activity: ComponentActivity) {

    companion object {
        private const val TAG = "FilePickerHelper"
        private const val READ_EXTERNAL_STORAGE_REQUEST = 1
    }

    private var filePickerLauncher: ActivityResultLauncher<Intent>? = null

    init {
        filePickerLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.also { uri ->
                    readFileAndPrintToConsole(uri)
                }
            }
        }
    }

    fun openFilePicker() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST)
        } else {
            launchFilePicker()
        }
    }

    private fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        filePickerLauncher?.launch(intent)
    }

    fun readFileAndPrintToConsole(uri: Uri) {
        try {
            activity.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        Log.d(TAG, line!!)
                        println(".cfg içerigi")
                        println(line)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Dosya okunurken hata oluştu", e)
            Toast.makeText(activity, "Dosya okunurken hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchFilePicker()
            } else {
                Toast.makeText(activity, "Dosya okuma izni reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
