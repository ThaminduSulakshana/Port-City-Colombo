package com.example.mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider

import com.google.firebase.database.FirebaseDatabase
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter

import java.io.File
import java.io.FileOutputStream

class EmployeeDetails : AppCompatActivity() {

    //Update Part
    private lateinit var tvId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvExperience: TextView
    private lateinit var tvSalary: TextView
    private lateinit var tvQual: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)


        initView()
        setValuesToViews()
        generatePdfReport()


        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empLname").toString(),
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
        val dbRef = FirebaseDatabase.getInstance().getReference("Careers").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this,"Your Data deleted",Toast.LENGTH_LONG).show()
            val intent = Intent(this,FetchDetails::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{error->
            Toast.makeText(this,"Deleting Err ${error}",Toast.LENGTH_LONG).show()

        }
    }
    //Fetching data part
    private fun initView(){
        tvId = findViewById(R.id.tvId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmail = findViewById(R.id.tvEmail)
        tvAge = findViewById(R.id.tvAge)
        tvPhone = findViewById(R.id.tvPhone)
        tvExperience = findViewById(R.id.tvExperience)
        tvSalary = findViewById(R.id.edtSalary)
        tvQual = findViewById(R.id.tvQual)




        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

    }

    private fun setValuesToViews(){

        tvId.text =intent.getStringExtra("empId")
        tvEmpName.text =intent.getStringExtra("empLname")
        tvEmail.text =intent.getStringExtra("empEmail")
        tvAge.text =intent.getStringExtra("empAge")
        tvPhone.text =intent.getStringExtra("empPhone")
        tvExperience.text =intent.getStringExtra("empExperience")
        tvSalary.text =intent.getStringExtra("empSalary")
        tvQual.text =intent.getStringExtra("empQual")





    }
    //Update Part


    private fun openUpdateDialog(

        empId :String,
        empLname:String

    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater =layoutInflater
        val mDialogView =inflater.inflate(R.layout.activity_update_dialog,null)

        mDialog.setView(mDialogView)
        //Intialize update dialog form input fields

        val edtLname = mDialogView.findViewById<EditText>(R.id.edtLname)
        val edtEmail = mDialogView.findViewById<EditText>(R.id.edtEmail)
        val edtAge = mDialogView.findViewById<EditText>(R.id.edtAge)
        val edtPhone = mDialogView.findViewById<EditText>(R.id.edtPhone)
        val edtExperience = mDialogView.findViewById<EditText>(R.id.edtExperience)
        val edtSalary = mDialogView.findViewById<EditText>(R.id.edtSalary)
        val edtQual = mDialogView.findViewById<EditText>(R.id.edtQual)

        val btnUpdateData =mDialogView.findViewById<Button>(R.id.btnSaveData)

        edtLname.setText(intent.getStringExtra("empLname").toString())
        edtEmail.setText(intent.getStringExtra("empEmail").toString())
        edtAge.setText(intent.getStringExtra("empAge").toString())
        edtPhone.setText(intent.getStringExtra("empPhone").toString())
        edtExperience.setText(intent.getStringExtra("empExperience").toString())
        edtSalary.setText(intent.getStringExtra("empSalary").toString())
        edtQual.setText(intent.getStringExtra("empQual").toString())

        mDialog.setTitle("Updating $empLname Record")

        val alertDialog=mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener{
            updateEmpData(
                empId,
                edtLname.text.toString(),
                edtEmail.text.toString(),
                edtAge.text.toString(),
                edtPhone.text.toString(),
                edtExperience.text.toString(),
                edtSalary.text.toString(),
                edtQual.text.toString()

            )
            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvEmpName.text = edtLname.text.toString()
            tvEmail.text = edtEmail.text.toString()
            tvAge.text = edtAge.text.toString()
            tvPhone.text = edtPhone.text.toString()
            tvExperience.text = edtExperience.text.toString()
            tvSalary.text = edtSalary.text.toString()
            tvQual.text = edtQual.text.toString()

            alertDialog.dismiss()

        }

    }
    private fun updateEmpData(
        id: String,
        Email:String,
        name: String,
        age: String,
        phone:String,
        Experience:String,
        salary:String,
        Qual:String,



    )
    {
        val dbRef = FirebaseDatabase.getInstance().getReference("Careers").child(id)
        val empInfo = EmployeeModel(
            id,
            Email,
            name,
            age,
            phone,
            Experience,
            salary,
            Qual
        )
        dbRef.setValue(empInfo)
    }
    //Job seeker can share his or her entered details pdf
    private fun generatePdfReport() {
        val fileName = "employee_report.pdf"
        val filePath = File(getExternalFilesDir(null), fileName)

        // Step 1: Create a new PDF document
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(filePath))

        // Step 2: Define the layout of the report
        document.open()
        document.add(Paragraph("Employee Report"))
        document.add(Paragraph("ID: ${intent.getStringExtra("empId")}"))
        document.add(Paragraph("Name: ${intent.getStringExtra("empLname")}"))
        document.add(Paragraph("Email: ${intent.getStringExtra("empEmail")}"))
        document.add(Paragraph("Age: ${intent.getStringExtra("empAge")}"))
        document.add(Paragraph("Phone: ${intent.getStringExtra("empPhone")}"))
        document.add(Paragraph("Experience: ${intent.getStringExtra("empExperience")}"))
        document.add(Paragraph("Salary: ${intent.getStringExtra("empSalary")}"))
        document.add(Paragraph("Qualification: ${intent.getStringExtra("empQual")}"))
        document.close()

        // Step 3: Save the PDF document to a file
        val contentUri = FileProvider.getUriForFile(
            this,
            "com.yourapp.fileprovider",
            filePath
        )
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "application/pdf"
        }

        // Step 4: Provide a way for the user to download or share the PDF file
        startActivity(Intent.createChooser(shareIntent, "Share Report"))
    }


}