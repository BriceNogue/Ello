package com.example.ello.main_package

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R
import com.example.ello.select_contact.ContactAdapter
import com.example.ello.select_contact.ContactModel
import com.example.ello.send_message.InboxSms

class MainAdapter (context: Context, var listDisc : MutableList<MainModel>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        //val cPicture: ImageView = itemView.findViewById(R.id.contact_picture)
        var uInfo = v.findViewById<TextView>(R.id.user_info)
        var msgPreview = v.findViewById<TextView>(R.id.msg_preview)

        /*init {
            v.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                val intent = Intent(v.context,InboxSms::class.java)
                startActivity(v.context,intent,null)
            }
        }*/

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.liste_de_discution, parent, false)

        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {

        var newList = listDisc[position]
        holder.uInfo.text = newList._address
        holder.msgPreview.text = newList._msg

    }

    override fun getItemCount(): Int {
        return 0
    }
}