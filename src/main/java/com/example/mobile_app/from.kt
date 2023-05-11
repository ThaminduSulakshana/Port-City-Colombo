package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.mobile_app.R.id.btnShow
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class from : AppCompatActivity() {

    // Declaring the UI elements as private lateinit vars
    private lateinit var selection_type: Spinner
    private lateinit var editTextPersonName: EditText
    private lateinit var editTextEmailAddress: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextReason: EditText
    private lateinit var checkBoxAgree: CheckBox
    private lateinit var btnSubmit: Button

    // Declaring the Firebase Database reference as a private lateinit var
    private lateinit var dbReference: DatabaseReference

    // Function called when the activity is created

    override fun onCreate(savedInstanceState: Bundle?) {
        // Hiding the status bar and the action bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        supportActionBar?.hide()

        // Calling the superclass's onCreate() function

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_from)


        // Initializing the UI elements
        selection_type = findViewById(R.id.selection_type)
        editTextPersonName = findViewById(R.id.editTextPersonName)
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextReason = findViewById(R.id.editTextReason)
        checkBoxAgree = findViewById(R.id.checkBoxAgree)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Initializing the Firebase Database reference to the 'sulayako' child
        dbReference = FirebaseDatabase.getInstance().getReference("sulayako")

        // Setting the click listener for the 'Submit' button
        btnSubmit.setOnClickListener {
            submitData()
        }

        // Setting the click listener for the 'Back' button
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Creating a new intent to switch to the 'MainActivity'
            val intent = Intent(this, helphome::class.java)
            // Starting the new activity
            startActivity(intent)
            // Finishing the current activity
            finish()
        }

        // Setting the click listener for the 'Show' button
        val btnShow = findViewById<Button>(btnShow)
        btnShow.setOnClickListener {
            // Creating a new intent to switch to the 'ShowDetails' activity
            val intent = Intent(this, ShowSupDetails::class.java)
            // Starting the new activity
            startActivity(intent)
            // Finishing the current activity
            finish()
        }

    }

    // Function to submit the data entered by the user to the Firebase Database
    private fun submitData(){

        // Getting the values entered by the user
        val empType = selection_type.selectedItem.toString()
        val empName = editTextPersonName.text.toString()
        val empEmail = editTextEmailAddress.text.toString()
        val empPhone = editTextPhone.text.toString()
        val empReason = editTextReason.text.toString()
        val agreeChecked = checkBoxAgree.isChecked

        // Checking if all the fields are filled
        if (empType.isEmpty() || empName.isEmpty() || empEmail.isEmpty() || empPhone.isEmpty() || empReason.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            return
        }

        // Checking if the user has agreed to the terms and conditions
        if (!agreeChecked) {
            Toast.makeText(this, "Please agree to the terms and Conditions", Toast.LENGTH_LONG).show()
            return
        }

        val emId = dbReference.push().key!!

        val sulayko = SupEmpModel(emId, empType, empName, empEmail, empPhone, empReason)

        dbReference.child(emId).setValue(sulayko)
            .addOnCompleteListener {
                Toast.makeText(this, "submitted successfully" , Toast.LENGTH_LONG).show()

                editTextPersonName.text.clear()
                editTextEmailAddress.text.clear()
                editTextPhone.text.clear()
                editTextReason.text.clear()
                selection_type.setSelection(0)
                checkBoxAgree.isChecked = false

            }.addOnFailureListener { error ->
                Toast.makeText(this, "Error ${error.message}" , Toast.LENGTH_LONG).show()
            }

    }
    }
