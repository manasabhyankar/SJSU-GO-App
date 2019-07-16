package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_sign_up)
        val spinner: Spinner = findViewById(R.id.department_spinner)
        // Create an ArrayAdapter using the string array and default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.department_major,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        /*****
         * does not seem very needed yet until the whole selecting the integration
         * this is going to be an on going working progress
        val spinner: Spinner = findViewById(R.id.academicYearDropdown)
        // Create an ArrayAdapter using the string array and default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.academicYear,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
         ******/
    }

    fun cancelClick(view: View) {
        //using onClickListener to trigger activity changes
        cancel_button.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun updateUI(user : FirebaseUser?){
        //TODO: Add user details to dashboard activity based on login status
    }

    fun signUpClick(view: View) {

        signup_button.setOnClickListener {
            doSignUp()
        }
    }
    private fun doSignUp(){
        if(signup_id.text.toString().isEmpty()){
            Toast.makeText(this, "Please enter a valid SJSU ID.", Toast.LENGTH_SHORT).show()
        }
        if(signup_id.text.toString().isEmpty()){
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
        }
        if(signup_pass.text.toString().isEmpty()){
            Toast.makeText(this, "Please enter a valid password.", Toast.LENGTH_SHORT).show()
        }
        if(first_name_text.text.toString().isEmpty()){
            Toast.makeText(this, "Please enter a first name.", Toast.LENGTH_SHORT).show()
        }
        if(last_name_text.toString().isEmpty()){
            Toast.makeText(this, "Please enter a last name.", Toast.LENGTH_SHORT).show()
        }

        else{
            firebaseAuth.createUserWithEmailAndPassword(signup_id.text.toString(), signup_pass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully created account. Welcome!", Toast.LENGTH_SHORT).show()
                        // Sign in success, update UI with the signed-in user's information
                        val user = firebaseAuth.currentUser
                        startActivity(Intent(this, DashboardActivity::class.java))

                    } else {
                        Toast.makeText(this, "Sign up failed! Please try again later.", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }
}
