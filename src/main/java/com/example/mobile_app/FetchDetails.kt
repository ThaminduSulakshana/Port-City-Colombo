package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class FetchDetails : AppCompatActivity() {

    //Read Data

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var empList:ArrayList<EmployeeModel>
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_details)


        empRecyclerView = findViewById(R.id.rvInfo)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData =findViewById(R.id.tvLoadingData)


        empList = arrayListOf<EmployeeModel>()

        getEmployeesData()

    }
    private fun getEmployeesData(){
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Careers")

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if(snapshot.exists()){
                    for(empSnap in snapshot.children){
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    //Click Employee Name and get His or her Info

                    mAdapter.setOnItemClickListener(object:EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent= Intent(this@FetchDetails, EmployeeDetails::class.java)


                            //put extras
                            intent.putExtra("empId", empList[position].empId)
                            intent.putExtra("empLname", empList[position].empLname)
                            intent.putExtra("empEmail", empList[position].empEmail)
                            intent.putExtra("empAge", empList[position].empAge)
                            intent.putExtra("empPhone", empList[position].empPhone)
                            intent.putExtra("empExperience", empList[position].empExperience)
                            intent.putExtra("empSalary", empList[position].empSalary)
                            intent.putExtra("empQual", empList[position].empQual)

                            startActivity(intent)


                        }

                    })

                    empRecyclerView.visibility =View.VISIBLE
                    tvLoadingData.visibility = View.GONE

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}