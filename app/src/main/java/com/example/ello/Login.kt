package com.example.ello

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ello.main_package.MainActivity
import com.example.ello.select_contact.SelectContact

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        var btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener(){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //onResum()
    }

    /*private fun enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@Login,
                arrayOf( Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS).toString()
            )
        ) {
            Toast.makeText(
                this@Login,
                "CONTACTS and SMS permission allows",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@Login, arrayOf(
                    Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS
                ), SelectContact.RequestPermissionCode
            )
        }
    }

    fun onResum() : Boolean{
        super.onResume()

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@Login,
                arrayOf( Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS).toString()
            )
        ) {
            Toast.makeText(
                this@Login,
                "CONTACTS and SMS permission allows",
                Toast.LENGTH_LONG
            ).show()
            return true
        } else {
            ActivityCompat.requestPermissions(
                this@Login, arrayOf(
                    Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS
                ), MainActivity.RequestPermissionCode
            )
            val viewGroup = findViewById<View>(R.id.log)
            viewGroup.visibility = View.GONE
            enableRuntimePermission()
            viewGroup.visibility = View.VISIBLE
            return false
        }
    }*/
}