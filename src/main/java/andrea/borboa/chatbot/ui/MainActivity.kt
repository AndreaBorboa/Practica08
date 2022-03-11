package andrea.borboa.chatbot.ui

import andrea.borboa.chatbot.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import andrea.borboa.chatbot.data.Message
import andrea.borboa.chatbot.utils.Constans.OPEN_GOOGLE
import andrea.borboa.chatbot.utils.Constans.OPEN_SEARCH
import andrea.borboa.chatbot.utils.Constans.RECEIVE_ID
import andrea.borboa.chatbot.utils.Constans.SEND_ID
import andrea.borboa.chatbot.utils.Time
import kotlinx.coroutines.*
import andrea.borboa.chatbot.utils.BotResponse


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessangingAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView()
        clickEvents()

        val random = (0..3).random()
        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
    }

    private fun clickEvents(){
        btn_send.setOnClickListener{
            sendMessage()
        }

        et_message.setOnClickListener {
            sendMessage()
        }

        et_message.setOnClickListener{
            GlobalScope.launch{
                delay(100)

                withContext(Dispatchers.Main){
                    rv_messages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessangingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                mv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage(){
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if(message.isNotEmpty()){
            messagesList.add(Message(message, SEND_ID, timeStamp))
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timestamp = Time.timeStamp()

        GlobalScope.launch {
            delay(100)

            withContext(Dispatchers.Main){
                val response = BotResponse.basicResponses(message)

                messagesList.add(Message(response, RECEIVE_ID, timestamp))
                adapter.insertMessage(Message(response, RECEIVE_ID, timestamp))
                rv_messages.scrollToPosition(adapter.itemCount - 1)
                when(response){
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH ->{
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?q=$searchTerm")
                        startActivity(site)
                    }
                }
            }
        }
    }

    private fun customBotMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insetmMssage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}