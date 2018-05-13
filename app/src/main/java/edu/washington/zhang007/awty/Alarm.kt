package edu.washington.zhang007.awty

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log

private const val TAG = "Alarm"

class Alarm : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val message = p1?.getStringExtra("MESSAGE")
        val phoneNumber = p1?.getStringExtra("PHONE")

        val sms = SmsManager.getDefault()

        sms.sendTextMessage(phoneNumber, null, message, null, null)
        Log.i(TAG, "Text Sent")
    }
}