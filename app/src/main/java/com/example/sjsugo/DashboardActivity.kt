package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }
    fun logoutClick(view: View) {
        //create an Intent to go back to Log In activity
        val logoutIntent = Intent(this, LoginActivity::class.java)

        //Going back to the start activity
        startActivity(logoutIntent)
    }
}
