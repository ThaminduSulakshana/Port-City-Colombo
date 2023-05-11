package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class EstimateBooking : AppCompatActivity() {

    private lateinit var tvId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvRes: TextView
    private lateinit var tvReg: TextView
    private lateinit var tvHotel: TextView
    private lateinit var tvComEmail: TextView
    private lateinit var tvFloor: TextView
    private lateinit var tvDes: TextView



    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimate_booking)


        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString(),
            )

        }
        btnDelete.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }
    }
    //Delete Records
    private fun deleteRecord(
        id:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Thilina").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this,"Your Data deleted", Toast.LENGTH_LONG).show()
            val intent = Intent(this,EstimateFetchActivty::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{error->
            Toast.makeText(this,"Deleting Err ${error}", Toast.LENGTH_LONG).show()

        }
    }
    //Fetching data part
    private fun initView(){
        tvId = findViewById(R.id.tvId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvPhone)
        tvRes = findViewById(R.id.tvRes)
        tvReg = findViewById(R.id.tvReg)
        tvHotel = findViewById(R.id.tvHotel)
        tvComEmail = findViewById(R.id.tvComEmail)
        tvFloor = findViewById(R.id.tvFloor)
        tvDes = findViewById(R.id.tvDes)





        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

    }

    private fun setValuesToViews(){

        tvId.text =intent.getStringExtra("empId")
        tvEmpName.text =intent.getStringExtra("empName")
        tvEmail.text =intent.getStringExtra("empEmail")
        tvPhone.text =intent.getStringExtra("empPhone")
        tvRes.text =intent.getStringExtra("empRes")
        tvReg.text =intent.getStringExtra("empReg")
        tvHotel.text =intent.getStringExtra("empHotel")
        tvComEmail.text =intent.getStringExtra("empComEmail")
        tvFloor.text =intent.getStringExtra("empFloor")
        tvDes.text =intent.getStringExtra("empDes")






    }
    //Update Part


    private fun openUpdateDialog(

        empId :String,
        empName:String

    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater =layoutInflater
        val mDialogView =inflater.inflate(R.layout.activity_estimate_update,null)

        mDialog.setView(mDialogView)
        //Intialize update dialog form input fields

        val edtName = mDialogView.findViewById<EditText>(R.id.edtName)
        val edtEmail = mDialogView.findViewById<EditText>(R.id.edtEmail)
        val edtPhone = mDialogView.findViewById<EditText>(R.id.edtPhone)
        val edtRes = mDialogView.findViewById<EditText>(R.id.edtRes)
        val edtReg = mDialogView.findViewById<EditText>(R.id.edtReg)
        val edtHotel = mDialogView.findViewById<EditText>(R.id.edtHotel)
        val edtComEmail = mDialogView.findViewById<EditText>(R.id.edtComEmail)
        val edtFloor = mDialogView.findViewById<EditText>(R.id.edtFloor)
        val edtDes = mDialogView.findViewById<EditText>(R.id.edtDes)

        val btnUpdateData =mDialogView.findViewById<Button>(R.id.btnUpdateData)

        edtName.setText(intent.getStringExtra("empName").toString())
        edtEmail.setText(intent.getStringExtra("empEmail").toString())
        edtPhone.setText(intent.getStringExtra("empPhone").toString())
        edtRes.setText(intent.getStringExtra("empRes").toString())
        edtReg.setText(intent.getStringExtra("empReg").toString())
        edtHotel.setText(intent.getStringExtra("empHotel").toString())
        edtComEmail.setText(intent.getStringExtra("empComEmail").toString())
        edtFloor.setText(intent.getStringExtra("empFloor").toString())
        edtDes.setText(intent.getStringExtra("empDes").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog=mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener{
            updateEmpData(
                empId,
                edtName.text.toString(),
                edtEmail.text.toString(),
                edtPhone.text.toString(),
                edtRes.text.toString(),
                edtReg.text.toString(),
                edtHotel.text.toString(),
                edtComEmail.text.toString(),
                edtFloor.text.toString(),
                edtDes.text.toString()


            )
            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvEmpName.text = edtName.text.toString()
            tvEmail.text = edtEmail.text.toString()
            tvPhone.text = edtPhone.text.toString()
            tvRes.text = edtRes.text.toString()
            tvReg.text = edtReg.text.toString()
            tvHotel.text = edtHotel.text.toString()
            tvComEmail.text = edtComEmail.text.toString()
            tvFloor.text = edtFloor.text.toString()
            tvDes.text = edtDes.text.toString()


            alertDialog.dismiss()

        }

    }
    private fun updateEmpData(
        id: String,
        Email:String,
        name: String,
        Phone:String,
        Res:String,
        Reg:String,
        Hotel:String,
        ComEmail:String,
        Floor:String,
        Des:String,
        )
    {
        val dbRef = FirebaseDatabase.getInstance().getReference("Thilina").child(id)
        val empInfo = EstimateEmployeeModel(id,Email, name,Phone,Res,Reg,Hotel,ComEmail,Floor,Des)
        dbRef.setValue(empInfo)
    }

}
