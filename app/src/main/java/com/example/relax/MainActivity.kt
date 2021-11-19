package com.example.relax

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.relax.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle : ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var binding : ActivityMainBinding
   lateinit var uid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
         uid = FirebaseAuth.getInstance().currentUser!!.uid.toString()

//        val uid = intent.getStringExtra("uid").toString()
        val firstFragment = ChannelFragment(uid,"why not to worry")
        val doesFragment = ChannelFragment(uid,"do's")
        val dontsFragment = ChannelFragment(uid,"dont's")
        val oxygenFragment = ChannelFragment(uid,"oxygen availability")
//        val doesFragment = doesFragment(uid)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,firstFragment)
            commit()
        }
        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarToggle = ActionBarDrawerToggle(this,drawerLayout,0,0);
        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        binding.actionBar.menuActionBar.setOnClickListener{
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        navView = findViewById(R.id.navView)
        val headerview = binding.navView.getHeaderView(0)
        val userview = headerview.findViewById<TextView>(R.id.user_name)
        applyUsername(userview)
        navView.setNavigationItemSelectedListener {menuItem->
            when (menuItem.itemId) {
                R.id.no_worry -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment,firstFragment)
                        commit()
                        drawerLayout.closeDrawer(Gravity.LEFT)
                    }
//                    Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.do_channel -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment,doesFragment)
                        commit()
                        drawerLayout.closeDrawer(Gravity.LEFT)
                    }
//                    Toast.makeText(this, "People", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.donts -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment,dontsFragment)
                        commit()
                        drawerLayout.closeDrawer(Gravity.LEFT)
                    }
//                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.oxygen->
                {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment,oxygenFragment)
                        commit()
                        drawerLayout.closeDrawer(Gravity.LEFT)
                        }
                    true
                }
                R.id.logout->
                {
                    AlertDialog.Builder(this)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to Log Out ?") // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes,
                            DialogInterface.OnClickListener { dialog, which ->
                                val session = Session(this)
                                session.logoutUser()
                                finishAffinity()
                                // Continue with delete operation
                            }) // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
    }

    // override the onBackPressed() function to close the Drawer when the back button is clicked
    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    fun applyUsername(utextview : TextView)
    {
        val mdb = FirebaseDatabase.getInstance().getReference()
        mdb.child("userlist").get().addOnSuccessListener {
            var name =it.child(uid).child("name").value.toString()
            utextview.text=name
            return@addOnSuccessListener
        }
    }

}