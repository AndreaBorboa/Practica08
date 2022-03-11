package andrea.borboa.chatbot.ui

import andrea.borboa.chatbot.R
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import andrea.borboa.chatbot.data.Message
import android.view.LayoutInflater

class MessangingAdapter:RecyclerView.Adapter<MessangingAdapter.MessageViewHolder>() {
    var messageList= mutableListOf<Message>()
    inner class MessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage=messageList[position]
        when(currentMessage.id){
            SEND_ID->{
                holder.itemView.tv_message.apply{
                    text=currentMessage.message
                    visibility=View.VISIBLE
                }
                holder.itemView.tv_message.visibility=View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    fun insertMessage(message:Message){
        this.messageList.add(message)
        notifyItemInserted(messageList.size)
        notifyDataSetChanged()
    }

}