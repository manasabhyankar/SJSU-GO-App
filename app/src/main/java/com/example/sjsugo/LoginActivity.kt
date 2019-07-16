package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
=======
import android.view.View
>>>>>>> cc48beecf1f3d7bcf0b811a77824d43a9136661a
import android.widget.Spinner

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
<<<<<<< HEAD
=======


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
>>>>>>> cc48beecf1f3d7bcf0b811a77824d43a9136661a
}
