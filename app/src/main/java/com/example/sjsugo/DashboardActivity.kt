package com.example.sjsugo

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        firebaseAuth = FirebaseAuth.getInstance()
        val logoutBtn = findViewById<Button>(R.id.logout_button)
        logoutBtn.setOnClickListener { doLogOut() }

//      THIS WILL DIRECT YOU TO THE ADD EVENT ACTIVITY
        val addeventBtn = findViewById<Button>(R.id.add_event_button)
        addeventBtn.setOnClickListener{startActivity(Intent(this, SubmitEventActivity::class.java))}
    }
    override fun onBackPressed() {
        //do nothing
    }
    /*
    1. Perform logging out
    2. Finish operations on DashboardActivity
    3. Return to LoginActivity
     */
    private fun doLogOut(){

        val logout_prompt = AlertDialog.Builder(this)
        logout_prompt.setTitle("Log out?")
        logout_prompt.setMessage("Do you wish to log out?")
        logout_prompt.setPositiveButton("Log Out") {dialog, which ->
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        logout_prompt.setNegativeButton("Cancel") {dialog, which ->
            dialog.cancel()
        }
        logout_prompt.show()
    }
}
