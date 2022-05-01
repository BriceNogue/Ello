package com.example.ello

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import java.util.regex.Matcher
import java.util.regex.Pattern

class SmsReceiver : BroadcastReceiver() {

    private var mListener: SmsListener? = null
    var p: Pattern = Pattern.compile("(|^)\\d{6}")

    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent!!.extras
        val pdus = data!!["pdus"] as Array<Any>?
        for (i in pdus!!.indices) {
            val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)
            val sender: String = smsMessage.getDisplayOriginatingAddress()
            val phoneNumber: String = smsMessage.getDisplayOriginatingAddress()
            val senderNum = phoneNumber
            val messageBody: String = smsMessage.getMessageBody()
            try {
                if (messageBody != null) {
                    val m: Matcher = p.matcher(messageBody)
                    if (m.find()) {
                        mListener!!.messageReceived(m.group(0))
                    }
                }
            } catch (e: java.lang.Exception) {
            }
        }
    }

    fun bindListener(listener: SmsListener) {
        mListener = listener
    }
}