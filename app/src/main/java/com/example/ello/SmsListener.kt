package com.example.ello

interface SmsListener {
    fun messageReceived(messageText: String?)
}