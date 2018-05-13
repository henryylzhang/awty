package edu.washington.zhang007.awty

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

private const val TAG = "Alarm"

class Alarm : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i(TAG, "onReceive called")

        val toastMessage = p1?.getStringExtra("MESSAGE")

        Toast.makeText(p0, toastMessage, Toast.LENGTH_SHORT).show()
    }
}