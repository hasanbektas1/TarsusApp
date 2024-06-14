package com.tarsus.tarsusapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tarsus.tarsusapp.ui.theme.TarsusAppTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1
        private const val JSON_FILE_PATH = "/storage/emulated/0/Download/videopath.json"
        private const val VIDEO_POSITION_KEY = "VIDEO_POSITION_KEY"
        private const val CURRENT_VIDEO_KEY = "CURRENT_VIDEO_KEY"
    }

    private lateinit var jsonFileReader: FileReaderHelper
    private var currentVideoKey: String = "video1"
    private var videoUris: Map<String, String> = mapOf()
    private var videoPositions: MutableMap<String, Int> = mutableMapOf()
    private var permissionsGranted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jsonFileReader = FileReaderHelper(JSON_FILE_PATH)

        // Check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
        } else {
            permissionsGranted = true
            readVideoPathsAndStartPlayer()
        }

        setContent {
            TarsusAppTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // Check permissions and videoUris before displaying the VideoPlayer
                    if (permissionsGranted && videoUris.isNotEmpty()) {
                        val videoUri by remember { mutableStateOf(Uri.parse(videoUris[currentVideoKey])) }
                        videoUri?.let {
                            VideoPlayer(
                                it,
                                videoPositions.getOrDefault(currentVideoKey, 0)
                            ) { pos -> videoPositions[currentVideoKey] = pos }
                        }
                    }
                }
            }
        }
    }

    private fun readVideoPathsAndStartPlayer() {
        val paths = jsonFileReader.readVideoPathsFromJson()
        if (paths != null) {
            videoUris = paths
            videoUris.keys.forEach { key ->
                videoPositions[key] = 0
            }
            updateTrVideo()
        } else {
            Toast.makeText(this, "Json dosyası okunamadı", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTrVideo() {
        videoPositions[currentVideoKey] = 0
        setContent {
            TarsusAppTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (permissionsGranted && videoUris.isNotEmpty()) {
                        val videoUri by remember { mutableStateOf(Uri.parse(videoUris[currentVideoKey])) }
                        videoUri?.let {
                            VideoPlayer(
                                it,
                                videoPositions.getOrDefault(currentVideoKey, 0)
                            ) { pos -> videoPositions[currentVideoKey] = pos }
                        }
                    }
                }
            }
        }
    }
    private fun updateIngVideo() {
        videoPositions[currentVideoKey] = 0
        setContent {
            TarsusAppTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (permissionsGranted && videoUris.isNotEmpty()) {
                        val videoUri by remember { mutableStateOf(Uri.parse(videoUris[currentVideoKey])) }
                        videoUri?.let {
                            VideoPlayer(
                                it,
                                videoPositions.getOrDefault(currentVideoKey, 0)
                            ) { pos -> videoPositions[currentVideoKey] = pos }
                        }
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = true
                readVideoPathsAndStartPlayer()
            } else {
                Toast.makeText(this, "Depolama izni reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                currentVideoKey = if (currentVideoKey == "video1") "video2" else "video2"
                updateTrVideo()
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                currentVideoKey = if (currentVideoKey == "video2") "video1" else "video1"
                updateIngVideo()
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                return true
            }
            else -> {
                return super.onKeyDown(keyCode, event)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        videoPositions[currentVideoKey]?.let { outState.putInt(VIDEO_POSITION_KEY, it) }
        outState.putString(CURRENT_VIDEO_KEY, currentVideoKey)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        videoPositions[currentVideoKey] = savedInstanceState.getInt(VIDEO_POSITION_KEY, 0)
        currentVideoKey = savedInstanceState.getString(CURRENT_VIDEO_KEY, "video1") ?: "video1"
    }
}
