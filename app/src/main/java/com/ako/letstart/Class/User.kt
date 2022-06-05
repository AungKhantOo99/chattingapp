package com.ako.letstart.Class

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(@PropertyName("uid")val uid:String,
                @PropertyName("imageuri")val Imageuri:String,
                @PropertyName("username")val username:String):Parcelable{
    constructor():this("","","")
}