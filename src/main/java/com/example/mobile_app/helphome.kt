package com.example.mobile_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class helphome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Set system UI visibility to layout full screen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Hide the action bar
        supportActionBar?.hide()

        // Call the superclass onCreate method
        super.onCreate(savedInstanceState)

        // Set the content view to activity_main.xml layout
        setContentView(R.layout.activity_helphome)

        // Get a reference to the Firebase database
        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        // Get a reference to the Contact button in the layout
        val btnContact = findViewById<Button>(R.id.btnContact);

        // Set a click listener on the Contact button
        btnContact.setOnClickListener() {
            // Create an Intent to go to the "from" activity
            val intenta = Intent(this, from::class.java)
            // Start the "from" activity
            startActivity(intenta)
            // Finish the current activity so the user can't go back to it
            finish()
        }

        // Get a reference to the FAQ button in the layout
        val btnFaq = findViewById<Button>(R.id.btnFaq);

        // Set a click listener on the FAQ button
        btnFaq.setOnClickListener() {
            // Create an Intent to go to the "faq" activity
            val intenta = Intent(this, faq::class.java)
            // Start the "faq" activity
            startActivity(intenta)
            // Finish the current activity so the user can't go back to it
            finish()
        }
    }
}