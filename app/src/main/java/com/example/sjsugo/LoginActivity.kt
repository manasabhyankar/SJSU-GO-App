package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        val signupBtn = findViewById<Button>(R.id.direct_to_signup)
        signupBtn.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
        val loginBtn = findViewById<Button>(R.id.log_in_button)
        loginBtn.setOnClickListener { doSignIn() }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(user: FirebaseUser?) {
        //TODO: Add user details to dashboard activity based on login status
        if (user != null) {
            val email = user.email.toString()
            val collectionRef = firebaseFirestore.collection("users")
            val documentRef = collectionRef.document(email)
            documentRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        val dashIntent = Intent(this, DashboardActivity::class.java)
                        dashIntent.putExtra("userName", document.get("first_name").toString().capitalize())
                        startActivity(dashIntent)
                        finish()
                    }
                }
            }
        }
    }

    private fun doSignIn() {
        if ((login_email.text.isNullOrEmpty() or login_pass.text.isNullOrEmpty())) {
            Toast.makeText(this, "Please enter valid credentials.", Toast.LENGTH_SHORT).show()
        } else {

            val email = login_email.text.toString()
            var firstName: String
            firebaseAuth.signInWithEmailAndPassword(login_email.text.toString(), login_pass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val collectionRef = firebaseFirestore.collection("users")
                        val documentRef = collectionRef.document(email)
                        documentRef.get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val document = task.result
                                if (document != null) {
                                    firstName = document.get("first_name").toString()
                                    Log.d("First name", firstName.toString())
                                    val dashIntent = Intent(this, DashboardActivity::class.java)
                                    dashIntent.putExtra("userName", firstName.capitalize())
                                    startActivity(dashIntent)
                                    finish()
                                } else {
                                    Log.d("Info: ", "No such document")
                                }
                            }
                        }


                    } else {
                        Log.d("Failure", "didn't work")
                        Toast.makeText(
                            this,
                            "Sign in failed! Please check your username and password.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }
    }

}
