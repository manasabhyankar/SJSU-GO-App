package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    //private lateinit var firebaseFirestore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        //firebaseFirestore = FirebaseFirestore.getInstance()
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
        val prompt = AlertDialog.Builder(this)
        prompt.setTitle("Incorrect email")
        prompt.setMessage("That email is already in use!")
        prompt.setNeutralButton("Ok") { _, _ ->
        }
//        logout_prompt.setPositiveButton("Log Out") {_, _ ->
//            firebaseAuth.signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
//        logout_prompt.setNegativeButton("Cancel") {dialog, _ ->
//            dialog.cancel()
//        }
        if (
            signup_id.text.toString().isNullOrEmpty() or
            signup_email.text.toString().isNullOrEmpty() or
            signup_pass.text.toString().isNullOrEmpty() or
            first_name.text.toString().isNullOrEmpty() or
            last_name.text.toString().isNullOrEmpty()
        ) {
            Toast.makeText(this, "One or more fields is empty. Please fill them all out.", Toast.LENGTH_SHORT).show()
        } else {

                firebaseAuth.createUserWithEmailAndPassword(signup_email.text.toString(), signup_pass.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successfully created account. Welcome!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()

                        } else {
                            if(task.exception is FirebaseAuthUserCollisionException){
                                prompt.show()
                                //Toast.makeText(this, "This email is already in use!", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                //Toast.makeText(this, task.exception., Toast.LENGTH_SHORT).show()
                                Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
                                updateUI(null)
                            }
                        }

                    }
        }
    }
}
//              firebaseFirestore.collection("users").add(user)
//                            .addOnSuccessListener {
//                                Toast.makeText(this, "Successfully created account. Welcome!", Toast.LENGTH_SHORT).show()
//                        }
//                            .addOnFailureListener{
//                                Toast.makeText(this, "Unable to add user data to Firestore. Please try again later!", Toast.LENGTH_SHORT)
//                        }
// Successful, then take remaining user data from fields and input to Firestore
//                        val user = hashMapOf(
//                            "email" to signup_email,
//                            "student_id" to signup_id,
//                            "first_name" to first_name,
//                            "last_name" to last_name
//                        )