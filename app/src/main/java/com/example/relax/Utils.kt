package com.example.relax

import android.content.Context
import android.text.TextUtils
import android.widget.Toast


  public fun toast(context : Context,message : String){
      Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
  }
public fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
