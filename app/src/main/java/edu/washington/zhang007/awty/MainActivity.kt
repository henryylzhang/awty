package edu.washington.zhang007.awty

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

                    val toastMessage = "${formatPhoneNumber(phone.toString())}: $msg"
                    i.putExtra("MESSAGE", toastMessage)

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
}
