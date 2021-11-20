package com.example.relax

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
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
        val activity = activity
        val title = activity!!.findViewById<TextView>(R.id.action_bar_text)
        title.text=channel
        val ll = ArrayList<String>()
        ll.add("mukul")
        val view =  super.onCreateView(inflater, container, savedInstanceState)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerview)
        val linearlayout = LinearLayoutManager(view.context)
//        linearlayout.isSmoothScrollbarEnabled=false
//        linearlayout.stackFromEnd=true
//        linearlayout.orientation=LinearLayoutManager.VERTICAL
//        recyclerView.layoutManager =linearlayout

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
                    val linearlayout = LinearLayoutManager(view.context)

                    linearlayout.orientation=LinearLayoutManager.VERTICAL
                    linearlayout.isSmoothScrollbarEnabled=true
//                    linearlayout.scrollToPosition(adapter2.itemCount-1)
                    linearlayout.stackFromEnd=true
//                    linearlayout.scrollToPositionWithOffset(adapter2.itemCount-1,0)
//                    linearlayout.reverseLayout=true
                    recyclerView.layoutManager =linearlayout
                    recyclerView.adapter = adapter2
                   var i=0;
                    while(i!=adapter2.itemCount)
                    {
                        val rv = recyclerView.adapter
                        i= rv!!.itemCount
                    }
//                    recyclerView.scrollToPosition(adapter2.itemCount-1)
                    recyclerView.smoothScrollToPosition(adapter2.itemCount)
//                    recyclerView.smoothScrollToPosition(adapter2.itemCount-1)
                } else {
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return view
    }

    override fun onStart() {
        super.onStart()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usermessage = view.findViewById<TextView>(R.id.message_input)
        val sendBtn = view.findViewById<Button>(R.id.send_button)

        if(channel=="welcome")
        {
            usermessage.maxLines=1
            usermessage.isEnabled=false
            usermessage.hint="You can't send messages to this channel"
            sendBtn.isVisible=false
            sendBtn.isClickable=false
        }
        FirebaseDatabase.getInstance().getReference("userlist").child(uid).get().addOnSuccessListener {
            username = it.child("name").value.toString()
        }
        val linearlayout = LinearLayoutManager(view.context)
//        linearlayout.stackFromEnd=true
//        linearlayout.orientation=LinearLayoutManager.VERTICAL
//        linearlayout.isSmoothScrollbarEnabled=true
//        recyclerView.layoutManager =linearlayout
//        val ref = FirebaseDatabase.getInstance().getReference("Messages").child(channel)
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val tudulist = ArrayList<String>()
//                    for (i in snapshot.children) {
//                        var tudu = i.child("name").value.toString()
//                        tudu = i.key.toString()
//                        tudulist.add(tudu)
//                    }
//                    val adapter2 = adapter(tudulist,uid,channel)
//                    val linearlayout = LinearLayoutManager(view.context)
//                    linearlayout.stackFromEnd=true
//                    linearlayout.orientation=LinearLayoutManager.VERTICAL
//                    linearlayout.isSmoothScrollbarEnabled=true
//                    recyclerView.layoutManager =linearlayout
//                    recyclerView.adapter = adapter2
//                    recyclerView.smoothScrollToPosition(adapter2.itemCount-1)
//                } else {
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
        sendBtn.setOnClickListener {
            val message = usermessage.text.toString()
            if(message.length==0)
                return@setOnClickListener
            val mdb : FirebaseDatabase = FirebaseDatabase.getInstance()
            val cur = uid
            val currentuser = FirebaseAuth.getInstance().currentUser
            val MessageList = mdb.getReference("Messages").child(channel)
            val id = MessageList.push().key.toString()
            val time = Calendar.getInstance().time
            var lastuser : String = "tudu"

                mdb.getReference("Messages").child(channel).get().addOnSuccessListener {
                    var luser : String="0"
                    var lmessage = it.child("uid")
                    var lastmessage : String = "0"
                    if(it.children.count()>0)
                    {
                        luser=it.children.elementAt((it.children.count()-1)).child("uid").value.toString()
                        lmessage =it.children.elementAt(it.children.count()-1)
                         lastmessage = lmessage.child("message").value.toString()
                        luser = lmessage.child("uid").value.toString()
                    }
                    if(username ==null)
                        username="guest"

                    if(luser!=null)
                    {
                        if(luser==uid)
                        {
                            val newmessage = lastmessage+"\n"+message
                            MessageList.child(lmessage.key.toString()).child("message").setValue(newmessage)
                        }
                        else
                        {
                            MessageList.child(id).child("message").setValue(message)
                            MessageList.child(id).child("username").setValue(username)
                            MessageList.child(id).child("uid").setValue(uid)
                        }
                    }

                }

            usermessage.text=""


        }
    }
}