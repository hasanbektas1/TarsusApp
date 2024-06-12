package com.tarsus.tarsusapp

import android.Manifest
import android.content.pm.PackageManager
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
    }

    private lateinit var jsonFileReader: FileReaderHelper
    private var videoPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jsonFileReader = FileReaderHelper(JSON_FILE_PATH)

        setContent {
            TarsusAppTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val videoUri by remember { jsonFileReader::videoUri }
                    // json içerisindeki yoldan videoyu oynat
                    videoUri?.let { VideoPlayer(it, videoPosition) { pos -> videoPosition = pos } }
                }
            }
        }

        // İzin verilmişse dosya okuma
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
        } else {
            readVideoPath()
        }
    }

    // Json dosyası içerigi okuma
    private fun readVideoPath() {
        if (!jsonFileReader.readVideoPathFromJson()) {
            Toast.makeText(this, "Json dosyası okunamadı", Toast.LENGTH_SHORT).show()
        }
    }
    // İzin isteme
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readVideoPath()
            } else {
                Toast.makeText(this, "Depolama izni reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Kumanda Tuşları
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(VIDEO_POSITION_KEY, videoPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        videoPosition = savedInstanceState.getInt(VIDEO_POSITION_KEY, 0)
    }

}
