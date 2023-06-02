package com.example.chatapp5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp5.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // 객체생성
    lateinit var binding:ActivityMainBinding
    lateinit var adapter:UserAdapter

    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
    //user list
    private lateinit var userList:ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=Firebase.auth
        database= Firebase.database.reference
        userList=ArrayList()

        adapter= UserAdapter(this,userList)

        binding.userRecycleView.layoutManager=LinearLayoutManager(this)
        binding.userRecycleView.adapter=adapter

        //사용자 불러오기
        database.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {// 데이터 변경시
                userList.clear()
                for(postSnapshot in snapshot.children){
                    //user info
                    val currentUser=postSnapshot.getValue(User::class.java)
                    //본인을 제외한 user 표시
                    if(auth.currentUser?.uid != currentUser?.uId){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()//화면에 적용
            }

            override fun onCancelled(error: DatabaseError) {//오류 발생시

            }

        })
    }
}