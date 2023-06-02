package com.example.chatapp5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp5.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatActivity : AppCompatActivity() {

    private lateinit var receiverName: String
    private lateinit var receiverUid: String

    //binding
    private  lateinit var binding:ActivityChatBinding

    lateinit var auth: FirebaseAuth
    lateinit var database:DatabaseReference

    private lateinit var receiverRoom:String//수신자 대화방
    private lateinit var senderRoom:String//발신자 대화방


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //넘어온 데이터 처리
        receiverName=intent.getStringExtra("name").toString()
        receiverUid=intent.getStringExtra("uid").toString()
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference

        //접속자 uid
        val senderUid=auth.currentUser?.uid

        //대화방 구분
        receiverRoom=senderUid+receiverUid
        senderRoom=receiverRoom+senderUid
        //대화상대 표시
        supportActionBar?.title=receiverName

        //전송 버튼 클릭 시
        binding.sendButton.setOnClickListener {
            val message=binding.inputChat.text.toString()
            val messageObject=Message(message,senderUid)

            //데이터 저장
            database.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //성공 시
                    database.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                    binding.inputChat.setText(null)
                }
        }
    }
}