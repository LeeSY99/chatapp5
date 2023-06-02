package com.example.chatapp5

data class Message(
    var message: String?,
    var sendId: String?,
){
    constructor():this("","")
}
