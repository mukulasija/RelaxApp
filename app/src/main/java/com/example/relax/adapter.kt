package com.example.relax

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class adapter(val items : ArrayList<String>, private val userId: String,val channel : String) : RecyclerView.Adapter<StudentViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_card,parent,false)
            val viewHolder = StudentViewHolder(view)
            return viewHolder
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            val ref = FirebaseDatabase.getInstance().getReference("Messages").child(channel)
            val currentItem = items[position]
            ref.child(currentItem).get().addOnSuccessListener {
                holder.username.text=it.child("username").value.toString()
                holder.message.text= it.child("message").value.toString()
                //                holder.titleView.text=  (position+1).toString() + ". "+ it.child("name").value.toString().capitalize()
            }

            // holder.titleView.text = currentItem.toString()
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }
    class StudentViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val username : TextView = itemview.findViewById(R.id.username)
        val message : TextView = itemview.findViewById(R.id.usermessage)
//        val titleView : TextView = itemview.findViewById(R.id.textView)
    }

    interface StudentItemClicked{
        fun onItemClicked(item : String)
    }

