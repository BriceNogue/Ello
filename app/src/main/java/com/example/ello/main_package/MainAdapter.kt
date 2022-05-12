package com.example.ello.main_package

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R
import com.example.ello.select_contact.ContactAdapter
import com.example.ello.select_contact.ContactModel
import com.example.ello.send_message.InboxSms
import java.lang.Exception

class MainAdapter (var context: Context, var listDisc : MutableList<MainConveration>, var listMsg : MutableList<MainModel>, var listCon : MutableList<ContactModel>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        //val cPicture: ImageView = itemView.findViewById(R.id.contact_picture)
        var uInfo = v.findViewById<TextView>(R.id.user_info)
        var msgPreview = v.findViewById<TextView>(R.id.msg_preview)
        var time = v.findViewById<TextView>(R.id.txt_time_main)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.liste_de_discution, parent, false)

        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {

        //listDisc.sortByDescending { it.date }
        var newList = listDisc!![position]
        //var newList1 = listMsg!![position]
        holder.uInfo.text = newList.number

        var lstMsg: MutableList<String>
        try {

            for (nbr in listCon){
                if (newList.number.equals(nbr.contact_number)){
                    holder.uInfo.text = nbr.contact_name
                    //Toast.makeText(context, "If...1...", Toast.LENGTH_SHORT).show()
                }
                else{
                     //holder.uInfo.text = newList.number
                    //Toast.makeText(context, "Else...", Toast.LENGTH_SHORT).show()
                }
            }

            lstMsg = mutableListOf()

            for (msg in listMsg){
                if (newList.number == msg._address){
                    lstMsg.add(msg._msg)
                    newList.date = msg._time
                    holder.msgPreview.text = lstMsg[0]
                    holder.time.text = msg._time

                    //Toast.makeText(context, "${newList.date}", Toast.LENGTH_SHORT).show()
                }
            }

        }catch (Ex: Exception){
            Toast.makeText(context, "${Ex.message}", Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener{ v:View ->
            val intent = Intent(v.context, InboxSms::class.java)
            intent.putExtra("phon", listDisc!![position].getAddress())
            //intent.putExtra("contact", listDisc!![position].getMsg())
            ContextCompat.startActivity(v.context, intent, null)
        }

    }

    override fun getItemCount(): Int {
        return listDisc.size
    }
}