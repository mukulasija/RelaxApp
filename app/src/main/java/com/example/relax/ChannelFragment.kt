package com.example.relax

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
 * Use the [ChannelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChannelFragment(public val uid: String, public val channel: String) : Fragment(R.layout.fragment_chat) {
    public var username : String = "guest"
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
//        linearlayout.isSmoothScrollbarEnabled=false
//        linearlayout.stackFromEnd=true
//        linearlayout.orientation=LinearLayoutManager.VERTICAL
//        recyclerView.layoutManager =linearlayout
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usermessage = view.findViewById<TextView>(R.id.message_input)
        val sendBtn = view.findViewById<Button>(R.id.send_button)
        toast(view.context,"on view created")

        FirebaseDatabase.getInstance().getReference("userlist").child(uid).get().addOnSuccessListener {
            username = it.child("name").value.toString()
        }
        val linearlayout = LinearLayoutManager(view.context)
        linearlayout.stackFromEnd=false
        linearlayout.orientation=LinearLayoutManager.VERTICAL
        linearlayout.isSmoothScrollbarEnabled=true
        recyclerView.layoutManager =linearlayout
        val ref = FirebaseDatabase.getInstance().getReference("Messages").child(channel)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tudulist = ArrayList<String>()
                    for (i in snapshot.children) {
                        var tudu = i.child("name").value.toString()
                        tudu = i.key.toString()
                        tudulist.add(tudu)
                    }
                    val adapter2 = adapter(tudulist,uid,channel)
                    recyclerView.adapter = adapter2

                    recyclerView.smoothScrollToPosition(adapter2.itemCount-1)
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
            val user = mdb.getReference("Messages").child(channel)
            val id = user.push().key.toString()
            val time = Calendar.getInstance().time
            var lastuser : String = "tudu"

                mdb.getReference("Messages").child(channel).get().addOnSuccessListener {
                    var luser : String="0"
                    if(it.children.count()>0)
                     luser=it.children.elementAt((it.children.count()-1)).child("uid").value.toString()
                    user.child(id).child("message").setValue(message)
                    if(username ==null)
                        username="guest"
                    user.child(id).child("username").setValue(username)
                    user.child(id).child("uid").setValue(uid)
                    user.child(id).child("showName").setValue("1")
                    if(luser!=null)
                    {
                        if(luser==uid)
                            user.child(id).child("showName").setValue("0")
                        toast(view.context,luser)
                    }

                }

//            toast(view.context,lastuser)
//            toast(view.context,lastuser)
            usermessage.text=""


        }
    }
}