package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        //get necessary Firebase instances
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        //logout button
        val logoutBtn = findViewById<Button>(R.id.logout_button)
        logoutBtn.setOnClickListener { doLogOut() }
        //get event submissions for user and display
        val userEvents = ArrayList<String>()
        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.email.toString())
            .collection("event_submissions").get().addOnSuccessListener { event_subs ->
                for (event in event_subs) {
                    //Log.d("Info", event.get("event_name").toString())
                    userEvents.add(event.get("event_name").toString())
                }
                val eventListing = findViewById<TextView>(R.id.event_listings)
                eventListing.text = "Your current pending event submissions:\n\n\n\n\n\n"
                Log.d("Empty arraylist", userEvents.size.toString())
                for (string in userEvents) {
                    Log.d("Info2", string)
                    eventListing.append(string.capitalize())
                    eventListing.append("\n\n")
                }
            }
        //display user's name on dashboard
        val actualName = intent?.getStringExtra("userName")
        findViewById<TextView>(R.id.dashboard_welcome).text = "Welcome " + actualName + "!"
        //add event button
        val addeventBtn = findViewById<Button>(R.id.add_event_button)
        addeventBtn.setOnClickListener { startActivity(Intent(this, SubmitEventActivity::class.java)) }

    }

    override fun onBackPressed() {
        //do nothing
    }

    /*
    1. Perform logging out
    2. Finish operations on DashboardActivity
    3. Return to LoginActivity
     */
    private fun doLogOut() {

        val logout_prompt = AlertDialog.Builder(this)
        logout_prompt.setTitle("Log out?")
        logout_prompt.setMessage("Do you wish to log out?")
        logout_prompt.setPositiveButton("Log Out") { _, _ ->
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        logout_prompt.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        logout_prompt.show()
    }
}
