package com.ako.letstart

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ako.letstart.Class.User
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

class AllMessageChatView : AppCompatActivity() {
 companion object{
     val uid=FirebaseAuth.getInstance().uid
     var CurrentUser: User?=null
 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title="Chatting"
        setContentView(R.layout.activity_all_message_chat_view)
        if(FirebaseAuth.getInstance().uid==null){
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            toast(FirebaseAuth.getInstance().uid.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.signout->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            R.id.selectchat->{
                startActivity(Intent(this,StartNewChat::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
