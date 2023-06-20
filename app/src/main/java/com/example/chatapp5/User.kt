package com.example.chatapp5

data class User(
    var name:String,
    var email:String,
    val uid:String,
){
    constructor():this("","","")
}

