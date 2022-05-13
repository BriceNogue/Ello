package com.example.ello.select_contact

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R
import com.example.ello.databinding.ActivitySelectContactBinding
import com.example.ello.main_package.MainActivity
import java.util.*
import java.util.Locale.getDefault


class SelectContact : AppCompatActivity() {

    private var cursor: Cursor? = null
    var name: String? = null
    private var phonenumber: String? = null
    lateinit var rcvSelectContact: RecyclerView
    var listContact: MutableList<ContactModel>? = null
    private lateinit var contactAdapter: ContactAdapter
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)

    }*/

    lateinit var binding: ActivitySelectContactBinding
    lateinit var newListC: MutableList<ContactModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newListC = mutableListOf()
        //newListC.add(ContactModel("alpha", "6767786"))
        listContact = mutableListOf()

        try {

            getContactsIntoMutableList()
            /*for (item in listContact!!) {
                newListC.add(item)
            }*/
            newListC.addAll(listContact!!)

            binding.searchViewSelectCont.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    /* keyWord = mutableListOf()
                     binding.searchViewSelectCont.clearFocus()
                     if (keyWord!!.contains(query)) {

                         listContact!!.filter { it.contact_name == query }
                         //getContact()

                     }*/

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val searchText = newText!!.toLowerCase(getDefault())
                    if (searchText.isNotEmpty()) {
                        //Toast.makeText(applicationContext, "${newListC.size}/n ${listContact!!.size}", Toast.LENGTH_SHORT).show()
                        newListC.forEach { name ->
                            newListC!!.filter { name.contact_name.contains(searchText.toLowerCase(Locale.getDefault())) }
                                listContact!!.clear()
                                listContact!!.add(name)
                                //Toast.makeText(applicationContext, "Add...", Toast.LENGTH_SHORT).show()
                        }

                        contactAdapter.notifyDataSetChanged()
                    } else {
                        listContact!!.clear()
                        listContact!!.addAll(newListC)
                        contactAdapter.notifyDataSetChanged()
                    }
                    return false
                }

            })

        } catch (Ex: java.lang.Exception) {
            Toast.makeText(this, "${Ex.message}", Toast.LENGTH_SHORT).show()
        }

        val btnReturn = findViewById<ImageButton>(R.id.btn_back_select_cont)
        btnReturn.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val recyclerview = findViewById<RecyclerView>(R.id.rcv_select_contact)
        recyclerview.layoutManager = LinearLayoutManager(this)

        //enableRuntimePermission()


        try {
            getContact()

        } catch (Ex: Exception) {
            Toast.makeText(this, "Authorize Ello to access your contacts", Toast.LENGTH_LONG).show()
        }


    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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

    private fun getContact() {
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
        contactAdapter.notifyDataSetChanged()
    }

    private fun getContactsIntoMutableList() {
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
            listContact!!.add(ContactModel("$name", "$phonenumber"))
            listContact!!.sortByDescending { it.contact_name }
            listContact!!.reverse()
        }
        cursor!!.close()
    }

    /*override fun onRequestPermissionsResult(RC: Int, per: Array<String>, PResult: IntArray) {
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
    }*/

    companion object {
        const val RequestPermissionCode = 1
    }


}