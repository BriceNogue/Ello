package com.example.ello.main_package

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R
import com.example.ello.select_contact.ContactModel
import com.example.ello.select_contact.SelectContact
import com.example.ello.send_message.NewMessage
import com.google.android.material.appbar.MaterialToolbar
import java.util.*


class MainActivity : AppCompatActivity() {

    /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.new_msg_app_bar, menu)
         return super.onCreateOptionsMenu(menu)

     }*/

    var cursor: Cursor? = null
    var name: String? = null
    private var phonenumber: String? = null
    var listContact: MutableList<ContactModel>? = null
    lateinit var rcvMain: RecyclerView
    lateinit var mainAdapter: MainAdapter
    var listConv: MutableList<MainConveration>? = null
    var listSms: MutableList<MainModel>? = null
    lateinit var txtMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /***************************** AppBarTop ************************/

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        /**********************************************************************/

       // onResum()

        //openSMSappChooser(this)
        //checkAndRequestPermissions()
        val rcvmain = findViewById<RecyclerView>(R.id.rcv_main)
        rcvmain.layoutManager = LinearLayoutManager(this)

        try {

            displaySms()

        } catch (Ex: Exception) {
            Toast.makeText(this, "Authorize Ello to access your message", Toast.LENGTH_LONG).show()
        }

        val btnNewMsg = findViewById<ImageButton>(R.id.btn_create_new_disc)

        btnNewMsg.setOnClickListener() {
            val intent = Intent(this, NewMessage::class.java)
            startActivity(intent)
            //enableRuntimePermission()
            finish()
        }

    }

    /*override fun onRequestPermissionsResult(RC: Int, per: Array<String>, PResult: IntArray) {
        super.onRequestPermissionsResult(RC, per, PResult)
        when (RC) {
            RequestPermissionCode -> if (PResult.isNotEmpty() && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@MainActivity,
                    "Permission Granted, Now your application can access message.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Permission Canceled, Now your application cannot access contact.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }*/

     private fun enableRuntimePermission() {
         if (ActivityCompat.shouldShowRequestPermissionRationale(
                 this@MainActivity,
                 Manifest.permission.SEND_SMS
             )
         ) {
             Toast.makeText(
                 this@MainActivity,
                 "CONTACTS and SMS permission allows",
                 Toast.LENGTH_LONG
             ).show()
         } else {
             ActivityCompat.requestPermissions(
                 this@MainActivity, arrayOf(
                     Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS
                 ), SelectContact.RequestPermissionCode
             )
         }
     }

    companion object {
        const val RequestPermissionCode = 1
    }

    fun openSMSappChooser(context: Context) {
        val packageManager = context.packageManager
        val componentName = ComponentName(context, MainActivity::class.java)
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val selector = Intent(Intent.ACTION_MAIN)
        selector.addCategory(Intent.CATEGORY_APP_MESSAGING)
        selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(selector)
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun checkAndRequestPermissions(): Boolean {
        val sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
        if (sms != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                RequestPermissionCode
            )
            return false
        }
        return true
    }

    //class Conversation(val number: String, val message: List<Message>)
    class Message(val number: String, val body: String, val date: Date)

    /* fun getSmsConversation(context: Context, number: String? = null, completion: (conversations: List<MainConveration>?) -> Unit) {
         val cursor = context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)

         val numbers = ArrayList<String>()
         val messages = ArrayList<Message>()
         var results = ArrayList<MainConveration>()

         while (cursor != null && cursor.moveToNext()) {
             val smsDate = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
             val number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
             val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))

             numbers.add(number)
             messages.add(Message(number, body, Date(smsDate.toLong())))
         }

         cursor?.close()

         numbers.forEach { number ->
             if (results.find { it.number == number } == null) {
                 val msg = messages.filter { it.number == number }
                 results.add(MainConveration(number = number, message = msg))
             }
         }

         if (number != null) {
             results = results.filter { it.number == number } as ArrayList<MainConveration>
         }

         completion(results)
     }*/

    @SuppressLint("Range")
    fun getAllSms() {

        txtMsg = findViewById(R.id.txt_msg_count)
        val numbers = ArrayList<String>()
        val messages = ArrayList<Message>()
        //var results = ArrayList<MainConveration>()

        var objSms: MainModel
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
        val totalSMS: Int = c!!.count
        if (c.moveToFirst()) {
            for (i in 0 until totalSMS) {
                objSms = MainModel()
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
                numbers.add(objSms._address)
                listSms!!.add(objSms)
                listSms!!.reverse()
                c.moveToNext()

            }

        } else {
            throw RuntimeException("You have no SMS");
        }
        c.close()

        var nbrMsg = 0
        numbers.forEach { number ->
            if (listConv!!.find { it.number == number } == null) {
                val msg = messages.filter { it.number == number }
                listConv!!.add(MainConveration(number = number, message = msg))
                //listConv!!.sortBy { it.number}
            }
            nbrMsg = listConv!!.size
            txtMsg.text = "$nbrMsg conversations"

        }
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
    }

    private fun displaySms() {
        rcvMain = findViewById(R.id.rcv_main)
        listConv = mutableListOf()
        listSms = mutableListOf()
        listContact = mutableListOf()

        getContactsIntoMutableList()

        getAllSms()

        mainAdapter = MainAdapter(
            this@MainActivity,
            listConv!!,
            listSms!!,
            listContact!!
        )
        rcvMain.adapter = mainAdapter
        mainAdapter.notifyDataSetChanged()
    }

    /* var listConv : MutableList<MainConveration>? = null
     var listSms: MutableList<MainModel>? = null
     @SuppressLint("Range")
     fun getAllSms(){
         var objSms : MainModel
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
         val totalSMS: Int = c!!.count
         if (c.moveToFirst()) {
             for (i in 0 until totalSMS) {
                 objSms = MainModel()
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
                 listSms!!.add(objSms)
                 listSms!!.reverse()
                 c.moveToNext()
             }
         }
         else {
             throw RuntimeException("You have no SMS");
         }
         c.close()

         var j = 0
         for (i in listSms!!){
             var temp: MainModel
             temp = listSms!![j]
             if (listSms!![j]._address.equals(temp._address)){

                 Toast.makeText(this, "${listSms!![j]._address}", Toast.LENGTH_SHORT).show()

             }
             j += 1
         }
     }

     lateinit var rcvMain : RecyclerView
     lateinit var mainAdapter : MainAdapter
     private fun displaySms(){
         rcvMain = findViewById(R.id.rcv_main)
         listSms = mutableListOf()

         //Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()

         getAllSms()

         mainAdapter = MainAdapter(
             this@MainActivity,
             listSms!!
         )
         rcvMain.adapter = mainAdapter
     }*/

    /* lateinit var rcvMain : RecyclerView
     lateinit var mainAdapter: MainAdapter
     lateinit var listSms: MutableList<MainModel>
     private fun displaySms(){
         rcvMain = findViewById(R.id.rcv_main)
         listSms = mutableListOf()

         getSmsConversation(this){ conversations ->
             conversations!!.forEach { conversation ->
                 Toast.makeText(this, "${conversation.number}",Toast.LENGTH_SHORT).show()
                 // println("Number: ${conversation.number}")
                 // println("Message One: ${conversation.message[0].body}")
                 // println("Message Two: ${conversation.message[1].body}")
             }
         }

         mainAdapter = MainAdapter(
             this@MainActivity,
             listSms!!
         )
         rcvMain.adapter = mainAdapter
     }*/

    /*override fun onResume() {
         super.onResume()
         val myPackageName = packageName
         if (Telephony.Sms.getDefaultSmsPackage(this) != myPackageName) {
             // App is not default.
             // Show the "not currently set as the default SMS app" interface
             val viewGroup = findViewById<View>(R.id.not_default_app)
             viewGroup.visibility = View.VISIBLE

             // Set up a button that allows the user to change the default SMS app
             val button: Button = findViewById<View>(R.id.change_default_app) as Button
             button.setOnClickListener(View.OnClickListener {
                 val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                 intent.putExtra(
                     Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                     myPackageName
                 )
                 startActivity(intent)
                 viewGroup.visibility = View.GONE
             })

         } else {
             // App is the default.
             // Hide the "not currently set as the default SMS app" interface
             val viewGroup = findViewById<View>(R.id.not_default_app)
             viewGroup.visibility = View.GONE
         }
     }*/


}