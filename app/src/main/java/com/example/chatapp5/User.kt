package com.example.chatapp5

data class User(
    var name:String,
    var email:String,
    val uId:String
){
    constructor():this("","","")
}

