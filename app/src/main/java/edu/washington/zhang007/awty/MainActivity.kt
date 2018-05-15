package edu.washington.zhang007.awty

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var MY_PERMISSIONS_REQUEST_SEND_SMS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // from Google Developer Documentation
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.d(TAG, "PERMISSION TO SEND SMS NOT GRANTED!")
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.SEND_SMS),
                    MY_PERMISSIONS_REQUEST_SEND_SMS)
        }
        // end from Google Developer Documentation

        // UI Declarations
        val msg = editText_message.text
        val phone = editText_phoneNumber.text
        val interval = editText_interval.text
        val startStop = button_startStop

        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(this, Alarm::class.java)

        startStop.setOnClickListener {
            if (startStop.text == "Start") {
                if (msg.isEmpty()) {
                    Toast.makeText(this,
                            "Message cannot be blank",
                            Toast.LENGTH_SHORT).show()
                } else if (phone.isEmpty() || phone.length != 10) {
                    Toast.makeText(this,
                            "Valid phone numbers have 10 digits",
                            Toast.LENGTH_SHORT).show()
                } else if (interval.isEmpty() || interval.toString() == "0") {
                    Toast.makeText(this,
                            "Interval must be > 0",
                            Toast.LENGTH_SHORT).show()
                } else {
                    startStop.text = "Stop"

                    i.putExtra("MESSAGE", msg.toString())
                    i.putExtra("PHONE", phone.toString())

                    val pi = PendingIntent.getBroadcast(this, 0,
                            i, PendingIntent.FLAG_UPDATE_CURRENT)

                    val intervalLong = interval.toString().toLong() * 1000 * 60
                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                            intervalLong, pi)
                }
            } else { // button.text = "Stop"
                startStop.text = "Start"

                val pi = PendingIntent.getBroadcast(this, 0,
                        i, PendingIntent.FLAG_CANCEL_CURRENT)

                am.cancel(pi)
            }
        }
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        val areaCode = phoneNumber.substring(0, 3)
        val prefix = phoneNumber.substring(3, 6)
        val lineNumber = phoneNumber.substring(6)

        return "($areaCode) $prefix-$lineNumber"
    }

    // from https://stackoverflow.com/questions/33666071/android-marshmallow-request-permission
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this@MainActivity, "Permission denied to send SMS", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }
    // end from Stack Overflow
}
