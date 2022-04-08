package com.example.ello.send_message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ello.R

class MessageAdapter(val context: Context) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }


    open inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var messageText = view.findViewById<TextView>(R.id.txtMyMessage)
        private var timeText = view.findViewById<TextView>(R.id.txtMyMessageTime)
        open fun bind(message: MessageModel) {
            messageText.text = message.message
            //timeText.text = fromMillisToTimeString(millis = message.time)
        }
    }


    open inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText = view.findViewById<TextView>(R.id.txtOrtherMessage)
        private var timeText = view.findViewById<TextView>(R.id.txtOrtherMessageTime)
        override fun bind(message: MessageModel) {
            messageText.text = message.message
           // timeText.text = fromMillisToTimeString(millis = message.time)
        }
    }

    private val messages: ArrayList<MessageModel> = ArrayList()

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
        val message = messages.get(position)
        holder.bind(message)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if (message.isSending) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}


