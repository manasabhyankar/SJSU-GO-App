package com.example.sjsugo

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
=======
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
>>>>>>> cc48beecf1f3d7bcf0b811a77824d43a9136661a
import android.widget.ArrayAdapter
import android.widget.Spinner

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val spinner: Spinner = findViewById(R.id.MajorDropdown)
        // Create an ArrayAdapter using the string array and default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.departmentMajor,
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

<<<<<<< HEAD

=======
    fun cancelClick(view: View) {
        //create an Intent to go back to Log In activity
        val cancelIntent = Intent(this, LoginActivity::class.java)

        //Going back to the start activity
        startActivity(cancelIntent)
    }

    fun signupClick(view: View) {
        //create an Intent to go back to Log In activity
        val signupIntent = Intent(this, DashboardActivity::class.java)

        //Going back to the start activity
        startActivity(signupIntent)
    }
>>>>>>> cc48beecf1f3d7bcf0b811a77824d43a9136661a
}
