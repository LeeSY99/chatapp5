package com.example.chatapp5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val receive=1 //수신은 1로 설정
    private val send=2 //발신은 2로 설정

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType ==1){//수신 화면
            val view:View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            ReceiveViewHolder(view)
         }else{//발신 화면
            val view:View = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
            SendViewHolder(view)
         }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { //data연결
        val currentMessage=messageList[position]//현재 메시지
        //data 전송
        if (holder.javaClass==SendViewHolder::class.java){
            val viewHolder=holder as SendViewHolder
            viewHolder.sendMessage.text=currentMessage.message
        }else{//data 수신
            val viewHolder=holder as ReceiveViewHolder
            viewHolder.receiveMessage.text=currentMessage.message
         }
    }

    override fun getItemViewType(position: Int): Int {
        //메시지 내용
        val currentMessage = messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)){
            send
        }else{
            receive
        }
    }
    //발신
    class SendViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sendMessage: TextView = itemView.findViewById(R.id.send_message)
    }
    //수신
    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receiveMessage: TextView= itemView.findViewById(R.id.receive_message)
    }
}