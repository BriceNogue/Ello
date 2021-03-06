package com.example.ello.send_message

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.main_package.MainActivity
import com.example.ello.R
import com.example.ello.main_package.MainAdapter
import com.example.ello.main_package.MainModel
import com.example.ello.select_contact.ContactModel

class InboxSms : AppCompatActivity() {

    lateinit var rcvIb : RecyclerView
    private lateinit var ibAdapter : MessageAdapter
    var cursor: Cursor? = null
    var name: String? = null
    private var phonenumber: String? = null
    var listContact: MutableList<ContactModel>? = null
    lateinit var btnSend: ImageButton
    lateinit var context: Context
    val smsManager = SmsManager.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox_sms)
        checkAndRequestPermissions()

        try {
            val btnBack = findViewById<ImageButton>(R.id.btn_back_inbox)
            btnBack.setOnClickListener(){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            val contactName = getIntent().getStringExtra("name")
            val contactPhone = getIntent().getStringExtra("contact")
            val phon = getIntent().getStringExtra("phon")

            //val contactpictue = findViewById<ImageView>(R.id.contact_picture_inbox)
            val phonn = findViewById<TextView>(R.id.contact_name_inbox)
            val contactname = findViewById<TextView>(R.id.contact_name_inbox)
            val contactphone = findViewById<TextView>(R.id.contact_phone_inbox)

            val rcv = findViewById<RecyclerView>(R.id.rcv_inbox)
            var layoutManager1 = LinearLayoutManager(this)
            layoutManager1.setReverseLayout(true)
            rcv.layoutManager = layoutManager1

            if (phon.isNullOrEmpty()){
                contactname.setText(contactName)
                displaySms(contactPhone!!)
            }else{
                phonn.setText(phon)
                displaySms(phon!!)
                for (cont in listContact!!){
                    if (cont.contact_number.equals(phon)){
                        phonn.setText(cont.contact_name)
                    }
                }
            }
            contactphone.setText(contactPhone)

            val btnSendInbox = findViewById<ImageButton>(R.id.btn_send_sms_inbox)
            val msgI = findViewById<EditText>(R.id.message_inbox)

            btnSendInbox.setOnClickListener(){
                var msg = msgI.text.toString()
                if (phon.isNullOrEmpty()){

                    sendSMS(contactPhone!!,msg)
                    msgI.text.clear()

                }else{

                    sendSMS(phon,msg)
                    msgI.text.clear()

                }
            }
        }catch (Ex : Exception){

        }

    }

    override fun onBackPressed(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkAndRequestPermissions(): Boolean {
        val sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
        if (sms != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                MainActivity.RequestPermissionCode
            )
            return false
        }
        return true
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

    var listSms: MutableList<MessageModel>? = null
    @SuppressLint("Range")
    fun getAllSms(numb : String){
        var objSms : MessageModel
        val uriSms = Uri.parse("content://sms")
        //smsList = mutableListOf()
        val c = this.contentResolver
            .query(
                uriSms, arrayOf(
                    "_id", "address", "date", "body",
                    "type", "read"
                ), "type", null,
                "date" + " COLLATE LOCALIZED ASC"
            )
        val totalSMS: Int = c!!.getCount()
        if (c.moveToLast()) {
            for (i in 0 until totalSMS) {
                objSms = MessageModel()
                objSms._id = (c.getString(c.getColumnIndexOrThrow("_id")))
                objSms._address = (
                        c.getString(
                            c.getColumnIndexOrThrow("address")
                        )
                        )
                objSms._msg = (c.getString(c.getColumnIndexOrThrow("body")))
                objSms._readState = (c.getString(c.getColumnIndex("read")))
                objSms._time = (c.getString(c.getColumnIndexOrThrow("date")))
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms._folderName = ("inbox")
                } else {
                    objSms._folderName = ("sent")
                }
                if (objSms._address.equals(numb)){

                    listSms!!.add(objSms)
                }
                //Toast.makeText(this, "${objSms._id}\n ${objSms._folderName}", Toast.LENGTH_SHORT).show()
                c.moveToPrevious()
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close()

    }

    private fun getContactsIntoMutableList() {
        cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            name =
                cursor!!.getString(cursor!!.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            phonenumber =
                cursor!!.getString(cursor!!.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            listContact!!.add(ContactModel("$name", "$phonenumber"))
            listContact!!.sortByDescending { it.contact_name }
            listContact!!.reverse()
        }
        cursor!!.close()
        //ibAdapter.notifyDataSetChanged()
    }

    private fun displaySms(numb: String){
        rcvIb = findViewById(R.id.rcv_inbox)
        listSms = mutableListOf()
        listContact = mutableListOf()

        //Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
        getContactsIntoMutableList()
        getAllSms(numb)

        ibAdapter = MessageAdapter(
            this@InboxSms,
            listSms!!,
            listContact!!
        )
        rcvIb.adapter = ibAdapter
        ibAdapter.notifyDataSetChanged()
    }

}