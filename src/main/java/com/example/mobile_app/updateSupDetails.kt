package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class updateSupDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_sup_details)

        val btnBack = findViewById<Button>(R.id.btnBack);

        btnBack.setOnClickListener() {
            val intent = Intent(this, Details::class.java)
            startActivity(intent)
            finish()
        }
    }
}