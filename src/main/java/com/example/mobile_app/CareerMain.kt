package com.example.mobile_app


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class CareerMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_career_main)


        var beach = findViewById<CardView>(R.id.Crd1)
        beach.setOnClickListener {
            val intent = Intent(this, CareerDetails::class.java)
            startActivity(intent)
    }}
}