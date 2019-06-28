package com.example.sjsugo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.Random as Random1

class RandomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)
        showRandomNumber()
    }
    companion object{
        const val TOTAL_COUNT = "total_count"
    }
    fun showRandomNumber(){
        val count = intent.getIntExtra(TOTAL_COUNT, 0)
        val random = java.util.Random()
        var randomInt = 0
        if(count > 0){
            randomInt = random.nextInt(count + 1)
        }
        findViewById<TextView>(R.id.textview_random).text = randomInt.toString()
        findViewById<TextView>(R.id.textview_label).text = getString(R.string.random_heading, count)
    }
}
