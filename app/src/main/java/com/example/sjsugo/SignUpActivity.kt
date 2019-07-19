package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_sign_up)
        val spinner: Spinner = findViewById(R.id.department_spinner)
        // Create an ArrayAdapter using the string array and default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.department_major,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val cancelBtn = findViewById<Button>(R.id.cancel_button)
        val signupBtn = findViewById<Button>(R.id.signup_button)
        cancelBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        signupBtn.setOnClickListener { doSignUp() }
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

    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun updateUI(user: FirebaseUser?) {
        //TODO: Add user details to dashboard activity based on login status
    }

    private fun doSignUp() {
        //TODO: Add additional user details to Firestore (name, email, etc)
        val error_prompt = AlertDialog.Builder(this)
        error_prompt.setTitle("Incorrect email")
        error_prompt.setMessage("That email is already in use!")
        error_prompt.setNeutralButton("Ok") { _, _ ->
        }
        /*
        Check if the input fields contain valid values. If not,
        tell user to fill them in completely. If they are, attempt
        sign up and user data storage and inform user of results.
         */
        if (
            signup_id.text.toString().isEmpty() or
            signup_email.text.toString().isEmpty() or
            signup_pass.text.toString().isEmpty() or
            first_name.text.toString().isEmpty() or
            last_name.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "One or more fields is empty. Please fill them all out.", Toast.LENGTH_SHORT).show()
        } else {
            //Attempt authentication query.
            val id = signup_id.text.toString()
            val email = signup_email.text.toString()
            val firstname = first_name.text.toString()
            val lastname = last_name.text.toString()
            firebaseAuth.createUserWithEmailAndPassword(signup_email.text.toString(), signup_pass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Successful, then take remaining user data from fields and input to Firestore
                        val user = hashMapOf(
                            "email" to email,
                            "student_id" to id,
                            "first_name" to firstname,
                            "last_name" to lastname
                        )
                        firebaseFirestore.collection("users").document(email).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Successfully created account. Welcome!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Unable to add user data to Firestore. Please try again later!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        val dashIntent = Intent(this, DashboardActivity::class.java)
                        dashIntent.putExtra("userName", firstname.capitalize())
                        startActivity(dashIntent)
                        finish()

                    } else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            error_prompt.show()
                            //Toast.makeText(this, "This email is already in use!", Toast.LENGTH_SHORT).show()
                        } else {
                            //Toast.makeText(this, task.exception., Toast.LENGTH_SHORT).show()
                            Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }

                }
        }
    }
}

