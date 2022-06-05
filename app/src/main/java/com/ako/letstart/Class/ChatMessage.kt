package com.ako.letstart.Class

class ChatMessage (val id:String,val text:String,val fromid:String,
                   val toid:String,val time:Long){
    constructor():this("","","","",1)
}