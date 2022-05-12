package com.example.ello.send_message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R
import com.example.ello.main_package.MainModel
import com.example.ello.select_contact.ContactModel
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class MessageAdapter(var context: Context, var listMsg : MutableList<MessageModel>, var listCon : MutableList<ContactModel>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }


    open inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var myMessage = view.findViewById<TextView>(R.id.txtMyMessage)
        private var timeText = view.findViewById<TextView>(R.id.txtMyMessageTime)
        open fun bind(message: MessageModel) {
            myMessage.text = message._msg
            timeText.text = message._time
        }
    }


    open inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var oMessageText = view.findViewById<TextView>(R.id.txtOrtherMessage)
        private var timeText = view.findViewById<TextView>(R.id.txtOrtherMessageTime)
        override fun bind(message: MessageModel) {
            oMessageText.text =  message._msg
            timeText.text = message._time
        }
    }

    //private val messages: ArrayList<MessageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.my_message, parent, false)
            return MessageViewHolder(view)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.other_message, parent, false)
            return OtherMessageViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        var newList = listMsg[position]
        holder.bind(newList)
    }

    override fun getItemViewType(position: Int): Int {
        val message = listMsg[position]

        return if (message._folderName == "sent") {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return listMsg.size
    }
}


