package com.example.mobile_app

// Importing necessary classes and methods
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

// Declaring a class named Details which extends AppCompatActivity
class Details : AppCompatActivity() {

    // Initializing TextView and Button variables
    private lateinit var TemID: TextView
    private lateinit var TempType: TextView
    private lateinit var TempName: TextView
    private lateinit var TempEmail: TextView
    private lateinit var TempPhone: TextView
    private lateinit var TempReason: TextView
    private lateinit var btngoUpdate: Button
    private lateinit var btnDelete: Button

    // Overriding onCreate() method of AppCompatActivity class
    override fun onCreate(savedInstanceState: Bundle?) {

        // Hiding the action bar and making the layout full screen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        supportActionBar?.hide()

        // Calling super class's onCreate() method
        super.onCreate(savedInstanceState)

        // Setting the activity layout file
        setContentView(R.layout.activity_sup_details)

        // Initializing TextView and Button variables with their respective views
        TemID = findViewById(R.id.TemID)
        TempType = findViewById(R.id.TempType)
        TempName = findViewById(R.id.TempName)
        TempEmail = findViewById(R.id.TempEmail)
        TempPhone = findViewById(R.id.TempPhone)
        TempReason = findViewById(R.id.TempReason)
        btngoUpdate = findViewById(R.id.btngoUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        // Setting values to views
        setValuesToViews()

        // Setting OnClickListener for update button
        btngoUpdate.setOnClickListener{
            // Passing intent extras to the update dialog
            openUpdateDialog(
                intent.getStringExtra("emID").toString(),
                intent.getStringExtra("empName").toString()

            )
        }

        // Setting OnClickListener for delete button
        btnDelete.setOnClickListener {
            // Displaying an alert dialog for confirmation of deletion
            AlertDialog.Builder(this)
                .setTitle("Delete Details")
                .setMessage("Are you sure you want to delete ?")
                .setPositiveButton("Yes") { _, _ ->
                    // Calling deleteRecord() method to delete the selected record from the database
                    deleteRecord(intent.getStringExtra("emID").toString())
                }
                .setNegativeButton("No", null)
                .show()
        }

        // Initializing back button and setting its OnClickListener
        val btnBack = findViewById<Button>(R.id.btnBack);
        btnBack.setOnClickListener() {
            // Creating an intent to navigate back to ShowDetails activity and finishing this activity
            val intent = Intent(this, ShowSupDetails::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Method to set values to TextViews
    private fun setValuesToViews() {

        // Getting intent extras and setting values to TextViews
        //(emId, empType, empName, empEmail, empPhone, empReason)
        TemID.text = intent.getStringExtra("emID")
        TempType.text = intent.getStringExtra("empType")
        TempName.text = intent.getStringExtra("empName")
        TempEmail.text = intent.getStringExtra("empEmail")
        TempPhone.text = intent.getStringExtra("empPhone")
        TempReason.text = intent.getStringExtra("empReason")
    }

    // This method is used to delete a record from the database
    private fun deleteRecord(
        id: String
    ){
        // Getting a reference to the record's node in the database
        val dbRef = FirebaseDatabase.getInstance().getReference("sulayako").child(id)

        // Removing the record from the database
        val mTask = dbRef.removeValue()

        // Adding success and failure listeners to the removeValue task
        mTask.addOnSuccessListener {
            Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ShowSupDetails::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    // This method is used to open the update dialog
    private fun openUpdateDialog(
        emID: String,
        empName: String
    ){
        // Creating a dialog builder and inflating the layout for the update dialog
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_update_sup_details, null)

        mDialog.setView(mDialogView)

        // Getting references to the EditText and Button views in the dialog
        val upType = mDialogView.findViewById<EditText>(R.id.upType)
        val upName = mDialogView.findViewById<EditText>(R.id.upName)
        val upEmail = mDialogView.findViewById<EditText>(R.id.upEmail)
        val upPhone = mDialogView.findViewById<EditText>(R.id.upPhone)
        val upReason = mDialogView.findViewById<EditText>(R.id.upReason)
        val btnUpdate = mDialogView.findViewById<Button>(R.id.btnUpdate)

        // Setting the existing values for the record in the EditText views
        upType.setText(intent.getStringExtra("empType").toString())
        upName.setText(intent.getStringExtra("empName").toString())
        upEmail.setText(intent.getStringExtra("empEmail").toString())
        upPhone.setText(intent.getStringExtra("empPhone").toString())
        upReason.setText(intent.getStringExtra("empReason").toString())

        mDialog.setTitle("Updating $empName Details")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdate.setOnClickListener{
            updateEmpData(
                emID,
                upType.text.toString(),
                upName.text.toString(),
                upEmail.text.toString(),
                upPhone.text.toString(),
                upReason.text.toString()
            )
            Toast.makeText(applicationContext, "Updated", Toast.LENGTH_LONG).show()

            // setting updated data to text view
            TempType.text = upType.text.toString()
            TempName.text = upName.text.toString()
            TempEmail.text = upEmail.text.toString()
            TempPhone.text = upPhone.text.toString()
            TempReason.text = upReason.text.toString()

            alertDialog.dismiss()
        }

    }

    // Function to update employee data in Firebase database
    private fun updateEmpData(
        id: String,
        type: String,
        name: String,
        email: String,
        phone: String,
        reason: String
    ) {
        // Get reference to the employee in the "sulayako" node of the Firebase database
        val dbRef = FirebaseDatabase.getInstance().getReference("sulayako").child(id)

        // Create an instance of the EmpModel data class with the updated employee information
        val empInfo = SupEmpModel(id, type, name, email, phone, reason)

        // Update the employee information in the Firebase database
        dbRef.setValue(empInfo)
    }

    // Function to save new employee data to Firebase database
    private fun saveData(
        type: String,
        name: String,
        email: String,
        phone: String,
        reason: String
    ) {
        // Get reference to the "sulayako" node in the Firebase database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("sulayako")

        // Generate a unique ID for the new employee using push().key
        val id = myRef.push().key

        // Create an instance of the EmpModel data class with the new employee information and the generated ID
        val empInfo = SupEmpModel(id.toString(), type, name, email, phone, reason)

        // Save the new employee information to the Firebase database under the generated ID
        myRef.child(id.toString()).setValue(empInfo)
            .addOnSuccessListener {
                // If the data is saved successfully, display a success message and start the ShowDetails activity
                Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ShowSupDetails::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener{
                // If the data fails to save, display an error message
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show()
            }
    }
}
