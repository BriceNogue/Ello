package com.example.ello.select_contact

data class ContactModel(var contact_name:String, var contact_number:String){

    fun getContactName(): String? {
        return contact_name
    }


    fun getContactNumber(): String? {
        return contact_number
    }



}
