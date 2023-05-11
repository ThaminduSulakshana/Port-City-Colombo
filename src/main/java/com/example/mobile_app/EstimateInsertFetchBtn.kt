package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EstimateInsertFetchBtn : AppCompatActivity() {
    private lateinit var btnInsert: Button
    private lateinit var btnFetch :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_insert_fetch_btn)

        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        btnInsert= findViewById<Button>(R.id.btnInsert)
        btnInsert.setOnClickListener {
            val next = Intent(this, hotelplot::class.java)
            startActivity(next)
        }
        btnFetch= findViewById<Button>(R.id.btnFetch)
        btnFetch.setOnClickListener {
            val next = Intent(this, EstimateFetchActivty::class.java)
            startActivity(next)
        }

    }
}