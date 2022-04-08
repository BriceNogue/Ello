package com.example.ello.send_message

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.MainActivity
import com.example.ello.R

class InboxSms : AppCompatActivity() {

    lateinit var btnSend: ImageButton
    lateinit var context: Context
    val smsManager = SmsManager.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox_sms)

        val btnBack = findViewById<ImageButton>(R.id.btn_back_inbox)

        btnBack.setOnClickListener(){
           val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        val rcv = findViewById<RecyclerView>(R.id.rcv_inbox)
        rcv.layoutManager = LinearLayoutManager(this)

        val contactName = getIntent().getStringExtra("name")
        val contactPhone = getIntent().getStringExtra("contact")
        val phon = getIntent().getStringExtra("phon")

       //val contactpictue = findViewById<ImageView>(R.id.contact_picture_inbox)
        val phonn = findViewById<TextView>(R.id.contact_name_inbox)
        val contactname = findViewById<TextView>(R.id.contact_name_inbox)
        val contactphone = findViewById<TextView>(R.id.contact_phone_inbox)

        if (phon.isNullOrEmpty()){
            contactname.setText(contactName)
        }else{
            phonn.setText(phon)
        }
        contactphone.setText(contactPhone)

        val btnSendInbox = findViewById<ImageButton>(R.id.btn_send_sms_inbox)
        val msg = findViewById<EditText>(R.id.message_inbox).text.toString()

        btnSendInbox.setOnClickListener(){
            if (phon.isNullOrEmpty()){

                sendSMS(contactPhone!!,msg)

            }else{

                sendSMS(phon,msg)

            }
        }

    }

    override fun onBackPressed(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

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