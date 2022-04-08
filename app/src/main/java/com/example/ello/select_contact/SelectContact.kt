package com.example.ello.select_contact

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R
import com.example.ello.send_message.NewMessage
import java.util.*

class SelectContact : AppCompatActivity() {

    private var cursor: Cursor? = null
    var name: String? = null
    private var phonenumber: String? = null

    lateinit var rcvSelectContact: RecyclerView
    var listContact: MutableList<ContactModel>? = null

    private lateinit var contactAdapter : ContactAdapter


   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_btn_top, menu)
        return super.onCreateOptionsMenu(menu)

    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_contact)


        val btnReturn = findViewById<ImageButton>(R.id.btn_bar_select_cont)
        btnReturn.setOnClickListener(){
            val intent = Intent(this, NewMessage::class.java)
            startActivity(intent)
            finish()
        }

        val recyclerview = findViewById<RecyclerView>(R.id.rcv_select_contact)
        recyclerview.layoutManager = LinearLayoutManager(this)

        getContact()
        enableRuntimePermission()


    }

   /* private fun getContacts() {
        //this method is use to read contact from users device.
        //on below line we are creating a string variables for our contact id and display name.
        var contactId = ""
        var displayName: String? = ""
        //on below line we are calling our content resolver for getting contacts
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        //on blow line we are checking the count for our cursor.
        if (cursor!!.count > 0) {
            //if the count is greater thatn 0 then we are running a loop to move our cursor to next.
            while (cursor.moveToNext()) {
                //on below line we are getting the phone number.
                val hasPhoneNumber =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .toInt()
                if (hasPhoneNumber > 0) {
                    //we are checking if the has phone number is >0
                    //on below line we are getting our contact id and user name for that contact
                    contactId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    displayName =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    //on below line we are calling a content resolver and making a query
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactId),
                        null
                    )
                    //on below line we are moving our cursor to next position.
                    if (phoneCursor!!.moveToNext()) {
                        //on below line we are getting the phone number for our users and then adding the name along with phone number in array list.
                        val phoneNumber =
                            phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        listContact!!.add(ContactModel(displayName, phoneNumber))
                    }
                    //on below line we are closing our phone cursor.
                    phoneCursor.close()
                }
            }
        }
        cursor.close()
        //on below line we are hiding our progress bar and notifying our adapter class.
        contactAdapter.notifyDataSetChanged()
    }*/

    private fun getContact(){
        rcvSelectContact = findViewById(R.id.rcv_select_contact)
        listContact = mutableListOf()

        getContactsIntoMutableList()

        contactAdapter = ContactAdapter(
            this@SelectContact,
            R.layout.contacts_list_model,
            R.id.contact_name,
            R.id.contact_number,
            listContact!!
        )
        rcvSelectContact!!.adapter = contactAdapter
    }


    private fun getContactsIntoMutableList(){
       /* cursor = contentResolver.query(
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
        cursor!!.close()*/
    }

    private fun enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@SelectContact,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            Toast.makeText(
                this@SelectContact,
                "CONTACTS permission allows us to Access CONTACTS app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@SelectContact, arrayOf(
                    Manifest.permission.READ_CONTACTS
                ), RequestPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(RC: Int, per: Array<String>, PResult: IntArray) {
        super.onRequestPermissionsResult(RC, per, PResult)
        when (RC) {
            RequestPermissionCode -> if (PResult.size > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@SelectContact,
                    "Permission Granted, Now your application can access CONTACTS.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@SelectContact,
                    "Permission Canceled, Now your application cannot access CONTACTS.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        const val RequestPermissionCode = 1
    }

}