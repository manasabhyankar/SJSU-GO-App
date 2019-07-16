package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(user : FirebaseUser?){
        //TODO: Add user details to dashboard activity based on login status
        startActivity(Intent(this, DashboardActivity::class.java))
    }
    fun signUpClick (view: View){
        //redirect to SignUpActivity
        direct_to_signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    fun loginClick (view: View){
        //set onClickListener to call the doSignIn method
        log_in_button.setOnClickListener {
            doSignIn()
        }
    }
    private fun doSignIn(){
        if((login_email.text.isNullOrEmpty() or login_pass.text.isNullOrEmpty())){
            Toast.makeText(this, "Please enter valid credentials.", Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(login_email.text.toString(), login_pass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("Success", "worked")
                        Toast.makeText(this, "Successfully logged in. Welcome!", Toast.LENGTH_SHORT).show()
                        // Sign in success, update UI with the signed-in user's information
                        val user = firebaseAuth.currentUser
                        startActivity(Intent(this, DashboardActivity::class.java))

                    } else {
                        Log.d("Failure", "didn't work")
                        Toast.makeText(this, "Sign in failed! Please check your username and password.", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }

}
