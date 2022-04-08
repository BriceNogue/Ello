package com.example.ello

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import java.security.cert.PKIXRevocationChecker

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        var btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener(){
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}