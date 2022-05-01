package com.example.ello.main_package

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
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
import com.example.ello.R
import com.example.ello.SmsMainModel
import com.example.ello.send_message.NewMessage


class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_btn_top, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openSMSappChooser(this)
        checkAndRequestPermissions()

        var btnNewMsg = findViewById<ImageButton>(R.id.btn_create_new_disc)

        btnNewMsg.setOnClickListener(){
            var intent = Intent(this, NewMessage::class.java)
            startActivity(intent)
            enableRuntimePermission()

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val myPackageName = packageName
            if (Sms.getDefaultSmsPackage(this) != myPackageName) {
                val intent = Intent(Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName)
                startActivityForResult(intent, 1)
            } else {
                val lst: List<SmsMainModel>? = getAllSms()
            }
        } else {
            val lst: List<SmsMainModel>? = getAllSms()
        }

    }

    override fun onRequestPermissionsResult(RC: Int, per: Array<String>, PResult: IntArray) {
        super.onRequestPermissionsResult(RC, per, PResult)
        when (RC) {
            RequestPermissionCode -> if (PResult.size > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
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

    private lateinit var mActivity : Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val myPackageName = packageName
                    if (Sms.getDefaultSmsPackage(mActivity) == myPackageName) {
                        val lst: List<SmsMainModel> = getAllSms()!!
                    }
                }
            }
        }
    }

    @SuppressLint("Range")
    fun getAllSms(): List<SmsMainModel>? {
        val lstSms: MutableList<SmsMainModel> = ArrayList()
        var objSms = SmsMainModel()
        val message: Uri = Uri.parse("content://sms/")
        val cr: ContentResolver = mActivity.getContentResolver()
        val c: Cursor? = cr.query(message, null, null, null, null)
        mActivity.startManagingCursor(c)
        val totalSMS: Int = c!!.getCount()
        if (c.moveToFirst()) {
            for (i in 0 until totalSMS) {
                objSms = SmsMainModel()
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")))
                objSms.setAddress(
                    c.getString(
                        c.getColumnIndexOrThrow("address")
                    )
                )
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")))
                objSms.setReadState(c.getString(c.getColumnIndex("read")))
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")))
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox")
                } else {
                    objSms.setFolderName("sent")
                }
                lstSms.add(objSms)
                c.moveToNext()
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close()
        return lstSms
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