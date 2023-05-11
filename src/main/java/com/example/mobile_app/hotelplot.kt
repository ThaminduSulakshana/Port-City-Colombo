package com.example.mobile_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class hotelplot : AppCompatActivity() {

    //Intialize Variables

    private lateinit var edtName : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPhone   : EditText
    private lateinit var edtRes  : EditText
    private lateinit var edtReg: EditText
    private lateinit var edtHotel : EditText
    private lateinit var edtComEmail : EditText
    private lateinit var edtFloor : EditText
    private lateinit var edtDes : EditText


    private lateinit var hotelPlotSubmitBtn : Button

    private lateinit var dbRef : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotelplot)

        edtName= findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        edtRes = findViewById(R.id.edtRes)
        edtReg= findViewById(R.id.edtReg)
        edtHotel = findViewById(R.id.edtHotel)
        edtComEmail = findViewById(R.id.edtComEmail)
        edtFloor = findViewById(R.id.edtFloor)
        edtDes = findViewById(R.id.edtDes)

        hotelPlotSubmitBtn =findViewById(R.id.btnUpdateData)

        dbRef = FirebaseDatabase.getInstance().getReference( "Thilina")

        hotelPlotSubmitBtn.setOnClickListener{
            saveCareerData()
        }
    }
    //Insert data to  Database
    private fun saveCareerData(){//Save Button
        //getting values

        val empName = edtName.text.toString()
        val empEmail = edtEmail.text.toString()
        val empPhone = edtPhone.text.toString()
        val empRes =edtRes.text.toString()
        val empReg = edtReg.text.toString()
        val empHotel = edtHotel.text.toString()
        val empComEmail = edtComEmail.text.toString()
        val empFloor = edtFloor.text.toString()
        val empDes = edtDes.text.toString()


//validation
        if(empName.isEmpty()){
            edtName.error ="Please enter "
        }
        if(empEmail.isEmpty()){
            edtEmail.error ="Please enter "
        }
        if(empPhone.isEmpty()){
            edtPhone.error ="Please enter "
        }
        if(empRes.isEmpty()){
            edtRes.error ="Please enter "
        }
        if(empReg.isEmpty()){
            edtReg.error ="Please enter"
        }
        if(empHotel.isEmpty()){
            edtHotel.error ="Please"
        }
        if(empComEmail.isEmpty()){
            edtComEmail.error ="Please enter"
        }
        if(empFloor.isEmpty()){
            edtFloor.error ="Please enter"
        }
        if(empDes.isEmpty()){
            edtDes.error ="Please enter"
        }



        val empId = dbRef.push().key!!

        val employee = EstimateEmployeeModel(empId,empName,empEmail,empPhone,empRes,empReg,empHotel,empComEmail,empFloor,empDes)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()

                //After click save button all the info deleted in the fields
                edtName.text.clear()
                edtEmail.text.clear()
                edtPhone.text.clear()
                edtRes.text.clear()
                edtReg.text.clear()
                edtHotel.text.clear()
                edtComEmail.text.clear()
                edtFloor.text.clear()
                edtDes.text.clear()





            }.addOnFailureListener {err->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }




    }
}