package com.example.ello.main_package

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

        openSMSappChooser(applicationContext)

        var btnNewMsg = findViewById<ImageButton>(R.id.btn_create_new_disc)

        btnNewMsg.setOnClickListener(){
            var intent = Intent(this, NewMessage::class.java)
            startActivity(intent)
            enableRuntimePermission()
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
}