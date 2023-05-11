package com.example.mobile_app

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.mobile_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityLoginBinding
    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog will show while creating account | Register user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait..")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click not have account, gate register screen
        binding.noAccountTv.setOnClickListener{
        startActivity(Intent(this, RegisterActivity::class.java))

    }
    //handle click, begin login
   binding.loginBtn.setOnClickListener {

       /* steps
    1) Input Data
    2) validate data
    3) login - firebase auth
    4) Check user type - firebase Auth
       if user - move to user dashboard
       if admin - move to admin  dashboard

     */
       validateData()
   }
}
    private var email = ""
    private var password = ""

    private fun validateData(){
        //1)input data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //2)validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email format..", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this, "Enter password..", Toast.LENGTH_SHORT).show()
        }
        else{
            loginUser()
        }
    }
    private fun loginUser() {
        //3)login - firebase Auth

        //show progress
        progressDialog.setMessage("Logging In ..")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
               checkUser()
            }
            .addOnFailureListener { e ->
                //failed login
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }
    private fun checkUser(){
        /*4) Check user type - firebase Auth
                if user - move to user dashboard
                if admin - move to admin  dashboard */

        progressDialog.setMessage("Checking User ...")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    //get user type e.g. user or admin
                    val userType = snapshot.child("userType").value
                    if (userType == "user") {
                        //its simple user, open user dashboard
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        finish()
                    } else if (userType == "admin") {
                        //its admin, open admin dashboard
                        startActivity(Intent(this@LoginActivity, DashboardAdminActivity::class.java))
                        finish()

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

    }
