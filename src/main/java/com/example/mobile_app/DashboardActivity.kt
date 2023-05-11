package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView



class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        var card1 = findViewById<CardView>(R.id.d1)
        card1.setOnClickListener {
            val intent = Intent(this, DashboardUserActivity::class.java)
            startActivity(intent)
        }
        var card2 = findViewById<CardView>(R.id.d2)
        card2.setOnClickListener {
            val intent = Intent(this, CareerMain::class.java)
            startActivity(intent)
        }
       var card3 = findViewById<CardView>(R.id.d3)
        card3.setOnClickListener {
            val intent = Intent(this, EstimateMainActivity::class.java)
            startActivity(intent)
        }
        var card4 = findViewById<CardView>(R.id.d4)
        card4.setOnClickListener {
            val intent = Intent(this, helphome::class.java)
            startActivity(intent)
        }
    }
}