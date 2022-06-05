package com.ako.letstart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ako.letstart.Class.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_start_new_chat.*
import kotlinx.android.synthetic.main.userlist.view.*
import org.jetbrains.anko.toast

class StartNewChat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_new_chat)
        supportActionBar!!.title="Select User"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        fetchUser(this)
    }
    fun fetchUser( context: Context){
        val ref=FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                   val user=it.getValue(User::class.java)
                    Log.d("image",user!!.Imageuri)
                    //toast(user!!.username)
                       if(use)
                   adapter.add(AllUser(user!!))
                }
                showalluser.adapter = adapter
                showalluser.layoutManager= LinearLayoutManager(context)
                adapter.setOnItemClickListener { item, view ->
                    val userdetail=item as AllUser
                    val intent=Intent(view.context,ChattingActivity::class.java)
                    intent.putExtra("Key",userdetail.user)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                  toast(error.toString())
                Log.d("error",error.message)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()

    }
}
class AllUser(val user:User):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
         viewHolder.itemView.username.text=user.username

        Picasso.get().load(user.Imageuri).into(viewHolder.itemView.userprofile)
    }

    override fun getLayout()=R.layout.userlist

}

