package com.example.mobile_app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class anotherPlot : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another_plot)

        val visitFacebookBtn = findViewById<Button>(R.id.button)

        // Set the onClickListener for the "Visit Facebook" button
        visitFacebookBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.portcitycolombo.lk/land-plots/#")
            startActivity(intent)
        }
    }
}