package com.example.ello.main_package

import java.util.*

data class MainConveration(val number: String, val message: List<MainActivity.Message>, var date : String? = null){

    fun getAddress(): String? {
        return number
    }

    @JvmName("getDate1")
    fun  getDate(): String?{
        return date!!
    }

}
