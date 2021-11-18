package com.example.relax

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.relax.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityLoginBinding
    private lateinit var session : Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        session = Session(this)
        if(session.isLoggedin())
        {
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("uid", session.getUserid())
            startActivity(intent)
            finish()
        }
        binding.signupBtn.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            val em= binding.loginEmailid.text.toString()
            val pass = binding.loginPassword.text.toString()

            val inputResult =  validateInput(em,pass)
            inputResult?.let {
                toast(this,inputResult)
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(em, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast(this,"Logged in Successfully")
                    // Toast.makeText(this, "tudu tudu", Toast.LENGTH_SHORT).show()
                    val usid = auth.currentUser!!.uid.toString()
                    val uid = FirebaseAuth.getInstance().currentUser!!.uid.toString()
                    toast(this,usid)
                    //toast(usid)
                    session.createLoginSession(usid)
                    val i = Intent(this, MainActivity::class.java)
                    i.putExtra("uid", usid)
                    startActivity(i)
                    finish()
                } else {
                    toast(this,"${task.exception?.message}")
                }
            }
//            auth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener {
//                if(it.isSuccessful)
//                {
//                    Toast.makeText(this,"user created",Toast.LENGTH_LONG).show()
//                }
//                else
//                {
//                    Toast.makeText(this, it.exception?.message,Toast.LENGTH_LONG).show()
//                }
//            }
        }


    }
    private fun validateInput(email: String, password: String): String? {

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

        return null
    }
}
