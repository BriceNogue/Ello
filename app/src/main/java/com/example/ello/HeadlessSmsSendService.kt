package com.example.ello

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable

class HeadlessSmsSendService : Service() {
    @Nullable
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}