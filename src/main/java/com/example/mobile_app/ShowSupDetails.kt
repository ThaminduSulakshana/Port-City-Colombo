package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ShowSupDetails : AppCompatActivity() {

    // Declare variables
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<SupEmpModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_sup_details)

        // Get references to the UI elements
        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        // Initialize the employee list
        empList = arrayListOf<SupEmpModel>()

        // Call the method to get employee data from Firebase
        getEmployeesData()
    }

    // Method to get employee data from Firebase
    private fun getEmployeesData() {
        // Set system UI visibility to layout full screen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Hide the action bar
        supportActionBar?.hide()

        // Get a reference to the Back button in the layout
        val btnBack = findViewById<Button>(R.id.btnBack);

        // Set a click listener on the Back button
        btnBack.setOnClickListener() {
            // Create an Intent to go back to the "from" activity
            val intent = Intent(this, from::class.java)
            // Start the "from" activity
            startActivity(intent)
            // Finish the current activity so the user can't go back to it
            finish()
        }

        // Hide the employee list RecyclerView and show the "Loading data..." TextView
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        // Get a reference to the "sulayako" node in the Firebase database
        dbRef = FirebaseDatabase.getInstance().getReference("sulayako")

        // Add a ValueEventListener to the database reference
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the employee list
                empList.clear()
                if (snapshot.exists()) {
                    // Loop through the employee data in the snapshot
                    for (empSnap in snapshot.children) {
                        // Get the employee data as an EmpModel object
                        val empData = empSnap.getValue(SupEmpModel::class.java)
                        // If the employee data isn't null, add it to the employee list
                        empData?.let { empList.add(it) }
                    }
                    // Create a new EmpAdapter with the employee list and set it on the RecyclerView
                    val mAdapter = SupEmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    // Set a click listener on the items in the RecyclerView
                    mAdapter.setonItemsClickListener(object : SupEmpAdapter.onItemsClickListener{
                        override fun onItemsClick(position: Int) {
                            // Create an Intent to go to the "Details" activity
                            val intent = Intent(this@ShowSupDetails, Details::class.java)

                            // Put extras(emId, empType, empName, empEmail, empPhone, empReason)
                            intent.putExtra("emID", empList[position].emID)
                            intent.putExtra("empType", empList[position].empType)
                            intent.putExtra("empName", empList[position].empName)
                            intent.putExtra("empEmail", empList[position].empEmail)
                            intent.putExtra("empPhone", empList[position].empPhone)
                            intent.putExtra("empReason", empList[position].empReason)
                            startActivity(intent)
                        }

                    })
                    // Show the RecyclerView and hide the "loading data" TextView
                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }
            // Show the "loading data" TextView with an error message
            override fun onCancelled(error: DatabaseError) {
                empRecyclerView.visibility = View.GONE
                tvLoadingData.visibility = View.VISIBLE
                tvLoadingData.text = "Error: ${error.message}"
            }
        })
    }
    }
