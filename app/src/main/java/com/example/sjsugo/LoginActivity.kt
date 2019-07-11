package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun signupClick (view: View){
        //create an intent to start the second activity
        val signupIntent = Intent(this, SignUpActivity::class.java)

        //start the new activity
        startActivity(signupIntent)
    }

    fun loginClick (view: View){
        //create an intent to start the second activity
        val loginIntent = Intent(this, DashboardActivity::class.java)

        //start the new activity
        startActivity(loginIntent)
    }
}
