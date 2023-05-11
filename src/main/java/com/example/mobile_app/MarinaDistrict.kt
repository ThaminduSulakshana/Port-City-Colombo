package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MarinaDistrict : AppCompatActivity() {
    private lateinit var residentialPlotBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marina_district)


        residentialPlotBtn= findViewById<Button>(R.id.residentialPlotBtn)
        residentialPlotBtn.setOnClickListener {
            val next = Intent(this, EstimateInsertFetchBtn::class.java)
            startActivity(next)
        }


        //MArina PLot Page -----------------
        // Get the reference to the "Start" button from the layout
        val marinaPlotBtn = findViewById<Button>(R.id.marinaPlotBtn)

        // Set the onClickListener for the "Start" button
        marinaPlotBtn.setOnClickListener(){
            // Create an intent to start the Dashboard activity
            val MarinaPLot = Intent(this, anotherPlot::class.java)
            // Start the Dashboard activity and finish the MainActivity
            startActivity(MarinaPLot)
            finish()
        }

    }
}