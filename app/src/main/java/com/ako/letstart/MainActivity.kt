package com.ako.letstart

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.ako.letstart.Class.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Signup.setOnClickListener { CreateUser() }
        userimage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        loginpage.setOnClickListener {
            signinxml.visibility = View.GONE
            loginxml.visibility = View.VISIBLE
        }
        Login.setOnClickListener { Login() }
    }
    private fun CreateUser() {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email.text.toString(), passward.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    toast("Sign Up Successful")
                    SaveImagetoFirebase()
                }
            }
    }
    private fun Login() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(loginemail.text.toString(), loginpassward.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful)
                {
                    val intent=Intent(this,AllMessageChatView::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    toast("Login in successful")
                    finish()
                }
                else toast("Email or passward id incorrect")
            }
    }
    var photourl: Uri? = null
    var bitmap: Bitmap? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            photourl = data.data
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photourl)
            val bitmapDrawable = BitmapDrawable(bitmap)
            userimage.setBackgroundDrawable(bitmapDrawable)
        }
    }
    private fun SaveImagetoFirebase() {
        toast("image success ${photourl}")
        val file = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$file")
        ref.putFile(photourl!!)
            .addOnSuccessListener {
                toast("Save to firebase")
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    toast(it.toString())
                    SaveUserToDatabase(it.toString())
                }
            }

    }
    private fun SaveUserToDatabase(ProfileImageurl: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid!!,   ProfileImageurl,name.text.toString())
        ref.setValue(user).addOnSuccessListener {
            toast("Success save to database")
            val intent=Intent(this,AllMessageChatView::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            toast("fail ")
        }
    }
}


