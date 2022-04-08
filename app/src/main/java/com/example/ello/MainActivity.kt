package com.example.ello

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ello.select_contact.SelectContact
import com.example.ello.send_message.NewMessage

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_btn_top, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            SelectContact.RequestPermissionCode -> if (PResult.size > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
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
                ), SelectContact.RequestPermissionCode
            )
        }
    }

}