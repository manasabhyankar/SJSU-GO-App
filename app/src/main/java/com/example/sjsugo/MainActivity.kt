package com.example.sjsugo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun toastMe(view: View){
        val myToast = Toast.makeText(this, "Toast!", Toast.LENGTH_SHORT)
        myToast.show()
    }
}
//this is a test comment to see if GitHub vcs is working