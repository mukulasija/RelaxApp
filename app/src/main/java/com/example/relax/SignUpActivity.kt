package com.example.relax

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.example.relax.databinding.ActivityLoginBinding
import com.example.relax.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        makeclickable()
        session = Session(this)
        auth = Firebase.auth
        val mAuth=FirebaseAuth.getInstance()
        binding.signUpBtn.setOnClickListener {

            val email = binding.userEmailId.text.toString()
            val password = binding.password.text.toString()
            val confirmpass = binding.confirmPassword.text.toString()

            val inputResult =  validateInput(email, password,confirmpass)
            inputResult?.let {
                toast(this,inputResult)
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                val usid = mAuth.currentUser?.uid.toString()
                val currentUser = mAuth.currentUser
                val username = email.dropLast(10)
                session.createLoginSession(usid)
                val ulist = FirebaseDatabase.getInstance()
                val user = ulist.getReference("userlist").child(usid)
                user.child("name").setValue(username)
                val ref = FirebaseDatabase.getInstance().getReference(usid)
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("uid",usid)
                startActivity(intent)
                toast(this,"Signed Up successfully")
                finishAffinity()
            }
        }
    }
    private fun makeclickable()
    {
        val spannableString = SpannableString(binding.alreadyUser.text)
        val clickableSpan = object : ClickableSpan()
        {
            override fun onClick(widget: View) {
                onBackPressed()
            }
        }
        spannableString.setSpan(clickableSpan,22,32, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        binding.alreadyUser.text = spannableString
        binding.alreadyUser.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun validateInput(email: String, password: String,confirmpass : String): String?{

        if (email.isEmpty()) {
            return "Email cannot be empty"
        }

        if (!email.isEmailValid()) {
            return "Email is not valid"
        }

        if (password.isEmpty()) {
            return "Password cannot be empty"
        }

        if (password.length < 8 || password.length >= 20) {
            return "Password must be at least 8 characters"
        }
        if(confirmpass!=password)
        {
            return "Password and Confirm Password should be same"
        }
        return null
    }

}