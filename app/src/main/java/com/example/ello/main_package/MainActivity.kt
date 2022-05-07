package com.example.ello.main_package

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony.Sms
import android.view.Menu
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R
import com.example.ello.send_message.NewMessage


class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_btn_top, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //openSMSappChooser(this)
        checkAndRequestPermissions()
        val rcvmain = findViewById<RecyclerView>(R.id.rcv_main)
        rcvmain.layoutManager = LinearLayoutManager(this)

        displaySms()

        val btnNewMsg = findViewById<ImageButton>(R.id.btn_create_new_disc)

        btnNewMsg.setOnClickListener(){
            val intent = Intent(this, NewMessage::class.java)
            startActivity(intent)
            enableRuntimePermission()

        }

    }

    override fun onRequestPermissionsResult(RC: Int, per: Array<String>, PResult: IntArray) {
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
    }

    fun enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.SEND_SMS
            )
        ) {
            Toast.makeText(
                this@MainActivity,
                "SMS permission allows us to Access SMS app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.BROADCAST_SMS,
                    Manifest.permission.RECEIVE_MMS
                ),  RequestPermissionCode
            )
        }
    }

    companion object {
        const val RequestPermissionCode = 1
    }

    fun openSMSappChooser(context: Context) {
        val packageManager: PackageManager = context.getPackageManager()
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
        val totalSMS: Int = c!!.getCount()
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
                //Toast.makeText(this, "${objSms._id}\n ${objSms._folderName}", Toast.LENGTH_SHORT).show()
                c.moveToNext()
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close()

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
    }

   /* override fun onResume() {
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
            })
        } else {
            // App is the default.
            // Hide the "not currently set as the default SMS app" interface
            val viewGroup = findViewById<View>(R.id.not_default_app)
            viewGroup.visibility = View.GONE
        }
    }*/

}