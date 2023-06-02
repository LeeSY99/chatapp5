package com.example.chatapp5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val context: Context, val userList:ArrayList<User>):
RecyclerView.Adapter<UserAdapter.UserViewHolder>(){



    //user_layout연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):UserViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)


        return UserViewHolder(view)
    }
    //data연결
    override fun onBindViewHolder(holder:UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nameText.text = currentUser.name
    }
    //리스트 개수
    override fun getItemCount(): Int {
        return userList.size
    }
    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val nameText: TextView = itemView.findViewById(R.id.name_text)
    }


}