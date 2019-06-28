package com.example.sjsugo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun toastMe(view: View){
        val myToast = Toast.makeText(this, "Toast!", Toast.LENGTH_SHORT)
        myToast.show()
    }
    fun count(view: View){
        val textCount = findViewById<TextView>(R.id.textView)
        val countValue = textCount.text.toString()
        var count: Int = Integer.parseInt(countValue)
        count++
        textCount.text = count.toString()

    }
    fun rando(view: View){
        val randomIntent = Intent(this, RandomActivity::class.java)
        val countS = textView.text.toString()
        var countVal: Int = Integer.parseInt(countS)
        randomIntent.putExtra(RandomActivity.TOTAL_COUNT, countVal)
        startActivity(randomIntent)
    }
}
//this is a test comment to see if GitHub vcs is working