package com.example.relax

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.relax.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle : ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val uid = intent.getStringExtra("uid").toString()
        val firstFragment = ChannelFragment(uid,"No worry")
        val doesFragment = ChannelFragment(uid,"does")
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
                R.id.settings -> {
//                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
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
}