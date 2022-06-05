package com.ako.letstart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ako.letstart.Class.ChatMessage
import com.ako.letstart.Class.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.android.synthetic.main.receivefrom.view.*
import kotlinx.android.synthetic.main.sentto.view.*
import kotlinx.android.synthetic.main.userlist.view.*
import org.jetbrains.anko.toast

class ChattingActivity : AppCompatActivity() {
    var LoginUser: User? =null

    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        LoginUser= intent.getParcelableExtra<User>("Key")
        supportActionBar!!.title=LoginUser!!.username
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        allchatlog.adapter = adapter

        allchatlog.layoutManager= LinearLayoutManager(this)
        sent.setOnClickListener {
            SaveMessagetoFirebase()
        }
       // fetCurrentUser()
        ListenMessage()
    }
    private fun ListenMessage(){
        val ref=FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatmessage=snapshot.getValue(ChatMessage::class.java)
                if(chatmessage!!.fromid == FirebaseAuth.getInstance().uid){
                    adapter.add(SentTo(LoginUser!!,chatmessage!!))
                }else{
                    adapter.add(ReceiveFrom(LoginUser!!,chatmessage!!))
                }


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun SaveMessagetoFirebase(){
        val ref=FirebaseDatabase.getInstance().getReference("/messages").push()
        val text= newmessage.text.toString()
        val fromid=FirebaseAuth.getInstance().uid
        val toid=LoginUser!!.uid
        val message=ChatMessage(ref.key!!,text,fromid!!,toid,System.currentTimeMillis()/1000)
        ref.setValue(message).addOnSuccessListener{
            toast("Success sent$text")
        }
    }
    override fun onSupportNavigateUp(): Boolean {
      //  return super.onSupportNavigateUp()
       // startActivity(Intent(this,AllMessageChatView::class.java))
        finish()
        return true
    }
}

class SentTo(val user:User,val message:ChatMessage): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(user.Imageuri).into(viewHolder.itemView.userlogo)
        viewHolder.itemView.sentmessage.text=message.text
    }

    override fun getLayout()=R.layout.sentto

}
class ReceiveFrom(val user:User,val message:ChatMessage): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(user.Imageuri).into(viewHolder.itemView.chatuserimage)
       viewHolder.itemView.receivemessage.text=message.text
    }

    override fun getLayout()=R.layout.receivefrom

}
