package com.example.relax

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.relax.Session.Companion.uid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [NoWorryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoWorryFragment(uid : String) : Fragment(R.layout.fragment_chat) {
    public val uid = uid
    public lateinit var username : String
    public lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ll = ArrayList<String>()
        ll.add("mukul")
        val view =  super.onCreateView(inflater, container, savedInstanceState)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerview)
        val linearlayout = LinearLayoutManager(view.context)
        linearlayout.stackFromEnd=true
        recyclerView.layoutManager =linearlayout
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usermessage = view.findViewById<TextView>(R.id.message_input)
        val sendBtn = view.findViewById<Button>(R.id.send_button)

        FirebaseDatabase.getInstance().getReference("userlist").child(uid).get().addOnSuccessListener {
            username = it.child("name").value.toString()
        }
        val ref = FirebaseDatabase.getInstance().getReference("Messages").child("No worry")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tudulist = ArrayList<String>()
                    for (i in snapshot.children) {

                        var tudu = i.child("name").value.toString()
                        tudu = i.key.toString()
                        tudulist.add(tudu)
                    }
                    val adapter2 = adapter(tudulist,uid,"No worry")
                    recyclerView.adapter = adapter2
                } else {
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        sendBtn.setOnClickListener {
            val message = usermessage.text.toString()
            if(message.length==0)
                return@setOnClickListener
            val mdb : FirebaseDatabase = FirebaseDatabase.getInstance()
            val cur = uid
            val currentuser = FirebaseAuth.getInstance().currentUser
            val user = mdb.getReference("Messages").child("No worry")
            val id = user.push().key.toString()
            val time = Calendar.getInstance().time
            user.child(id).child("message").setValue(message)
            user.child(id).child("username").setValue(username)
            usermessage.text=""
        }
    }
}