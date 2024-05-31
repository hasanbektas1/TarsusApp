package com.tarsus.tarsusapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// Uygulamanın otomatik başlatılması
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val myIntent = Intent(context, MainActivity::class.java)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(myIntent)
        }
    }
}