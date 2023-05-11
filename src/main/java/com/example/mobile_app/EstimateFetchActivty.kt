package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class EstimateFetchActivty : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var empList:ArrayList<EstimateEmployeeModel>
    private lateinit var dbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_fetch_activty)


        empRecyclerView = findViewById(R.id.rvInfo)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData =findViewById(R.id.tvLoadingData)


        empList = arrayListOf<EstimateEmployeeModel>()

        getEmployeesData()
    }
    private fun getEmployeesData(){
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Thilina")

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if(snapshot.exists()){
                    for(empSnap in snapshot.children){
                        val empData = empSnap.getValue(EstimateEmployeeModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EstimateEmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    //Click Employee Name and get His or her Info

                    mAdapter.setOnItemClickListener(object: EstimateEmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent= Intent(this@EstimateFetchActivty, EstimateBooking::class.java)


                            //put extras
                            intent.putExtra("empId", empList[position].empId)
                            intent.putExtra("empName", empList[position].empName)
                            intent.putExtra("empEmail", empList[position].empEmail)
                            intent.putExtra("empPhone", empList[position].empPhone)
                            intent.putExtra("empRes", empList[position].empRes)
                            intent.putExtra("empReg", empList[position].empReg)
                            intent.putExtra("empHotel", empList[position].empHotel)
                            intent.putExtra("empComEmail", empList[position].empComEmail)
                            intent.putExtra("empFloor", empList[position].empFloor)
                            intent.putExtra("empDes", empList[position].empDes)


                            startActivity(intent)


                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
    }
