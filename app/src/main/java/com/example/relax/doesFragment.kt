package com.example.relax

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [doesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class doesFragment : Fragment(R.layout.fragment_does) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(context, "on create", Toast.LENGTH_SHORT).show()
        val view =  super.onCreateView(inflater, container, savedInstanceState)
        val textview : TextView = view!!.findViewById<TextView>(R.id.textviewdoes)
        textview.setText("this is fragment do's")
        return view
    }


}