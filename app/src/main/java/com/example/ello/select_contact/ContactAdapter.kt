package com.example.ello.select_contact

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.send_message.InboxSms
import com.example.ello.R

class ContactAdapter(c: Context, var layout: Int, var Name:Int, var Code:Int, var contactList: MutableList<ContactModel>?) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        //val cPicture: ImageView = itemView.findViewById(R.id.contact_picture)
        val cName = v.findViewById<TextView>(R.id.contact_name)
        val cNum = v.findViewById<TextView>(R.id.contact_number)

        /*init {
            v.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                val intent = Intent(v.context,InboxSms::class.java)
                startActivity(v.context,intent,null)
            }
        }*/

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_list_model, parent, false)

        return ContactViewHolder(view)
    }
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val newList = contactList!![position]

       // holder.cPicture.setImageResource(newList.contact_picture)
        holder.cName.text = newList.contact_name
        holder.cNum.text = newList.contact_number
        holder.itemView.setOnClickListener{ v:View ->
            val intent = Intent(v.context, InboxSms::class.java)
            intent.putExtra("name", contactList!![position].getContactName())
            intent.putExtra("contact", contactList!![position].getContactNumber())
            startActivity(v.context,intent,null)
        }
    }


    override fun getItemCount(): Int {
        return contactList!!.size
    }


}