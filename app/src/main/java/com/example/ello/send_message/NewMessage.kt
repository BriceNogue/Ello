package com.example.ello.send_message

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsManager
import android.view.Menu
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ello.R
import com.example.ello.main_package.MainActivity
import com.example.ello.select_contact.SelectContact

class NewMessage : AppCompatActivity() {

    lateinit var btnAddRec: ImageButton
    lateinit var btnSend: ImageButton
    lateinit var context: Context
    val smsManager = SmsManager.getDefault()

   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        val btnReturn = findViewById<ImageButton>(R.id.btn_back_new_msg)
        btnReturn.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnAddRec = findViewById(R.id.add_recever)
        btnAddRec.setOnClickListener() {
            val intent = Intent(this, SelectContact::class.java)
            startActivity(intent)
            finish()
        }

        /******************************************************* Appel de la fonction d'envoi de sms simple *************************************************/
        btnSend = findViewById(R.id.send_sms)
        val phoneN = findViewById<EditText>(R.id.add_number)
        val messageN = findViewById<EditText>(R.id.add_message)
        val phon = findViewById<EditText>(R.id.add_number)

        btnSend.setOnClickListener {
            val phone = phoneN.text.toString()
            val message = messageN.text.toString()

            val intent = Intent(this, InboxSms::class.java)
            intent.putExtra("phon", phon.getText().toString())

            if (phoneN.text.isEmpty()) {

                Toast.makeText(this, "Enter the phone number", Toast.LENGTH_SHORT).show()

            } else if (messageN.text.isEmpty()) {

                Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show()

            } else {

                sendSMS(phone, message)
                startActivity(intent)
                finish()

            }
        }
    }

    override fun onBackPressed(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /***************************************************** Fonction d'envoi de sms simple *************************************************************
     * ************************************************************************************************************************************************/

    private fun sendSMS(phoneNumber: String, message: String) {
        //enableRuntimePermission()
        val SENT = "SMS_SENT"
        val DELIVERED = "SMS_DELIVERED"
        val sentPI = PendingIntent.getBroadcast(this, 0, Intent(SENT), 0)
        val deliveredPI = PendingIntent.getBroadcast(this, 0, Intent(DELIVERED), 0)

        // ---when the SMS has been sent---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    RESULT_OK -> Toast.makeText(
                        baseContext, "SMS sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                        baseContext, "Generic failure",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                        baseContext, "No service",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
                        baseContext, "Null PDU",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
                        baseContext, "Radio off",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, IntentFilter(SENT))

        // ---when the SMS has been delivered---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    RESULT_OK -> Toast.makeText(
                        baseContext, "SMS delivered",
                        Toast.LENGTH_SHORT
                    ).show()
                    RESULT_CANCELED -> Toast.makeText(
                        baseContext, "SMS not delivered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, IntentFilter(DELIVERED))
        //val sms = SmsManager.getDefault()

        try {
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
        } catch (ex: Exception) {
            Toast.makeText(
                applicationContext, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
        }
    }

}


