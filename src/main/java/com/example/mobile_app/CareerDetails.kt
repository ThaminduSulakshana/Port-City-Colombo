package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CareerDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_career_details)

        val click = findViewById<Button>(R.id.btnApply)
        click.setOnClickListener {

            val intentApply = Intent(this, insertandfetch::class.java)
            startActivity(intentApply)
        }
    }
}