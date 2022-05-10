package com.example.ello.select_contact

import android.database.Cursor
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R

class SelectContactCO : AppCompatActivity(){

    private var cursor: Cursor? = null
    var name: String? = null
    private var phonenumber: String? = null

    lateinit var rcvSelectContact: RecyclerView
    var listContact: MutableList<ContactModel>? = null

    private lateinit var contactAdapter : ContactAdapter

    private fun getContact(){
        rcvSelectContact = findViewById(R.id.rcv_select_contact)
        listContact = mutableListOf()

        getContactsIntoMutableList()

        contactAdapter = ContactAdapter(
            this,
            R.layout.contacts_list_model,
            R.id.contact_name,
            R.id.contact_number,
            listContact!!
        )
        rcvSelectContact!!.adapter = contactAdapter
    }


    private fun getContactsIntoMutableList(){
        cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            name =
                cursor!!.getString(cursor!!.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            phonenumber =
                cursor!!.getString(cursor!!.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            listContact!!.add(ContactModel("$name","$phonenumber"))
            listContact!!.sortByDescending { it.contact_name }
            listContact!!.reverse()
        }
        cursor!!.close()
    }
}