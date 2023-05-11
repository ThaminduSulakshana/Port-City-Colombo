package com.example.mobile_app


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BookTicket : AppCompatActivity() {

    private lateinit var cardNumberEditText: EditText
    private lateinit var amountNumberEditText:EditText
    private lateinit var expiryMonthEditText: EditText
    private lateinit var expiryYearEditText: EditText
    private lateinit var cvvEditText: EditText

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ticket)

        val headerTextView = findViewById<TextView>(R.id.header_textview)
        headerTextView.text = "Payment Form"

        cardNumberEditText = findViewById(R.id.card_number_editText)
        amountNumberEditText= findViewById(R.id.amount_number_editText)
        expiryMonthEditText = findViewById(R.id.expiry_month_editText)
        expiryYearEditText = findViewById(R.id.expiry_year_editText)
        cvvEditText = findViewById(R.id.cvv_editText)

        val payButton = findViewById<Button>(R.id.pay_button)

        dbRef = FirebaseDatabase.getInstance().getReference( "Books")


        payButton.setOnClickListener {
            val cardNumber = cardNumberEditText.text.toString()
            val amountNumber = amountNumberEditText.toString()
            val expiryMonth = expiryMonthEditText.text.toString()
            val expiryYear = expiryYearEditText.text.toString()
            val cvv = cvvEditText.text.toString()

            // TODO: Implement payment processing logic

            Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
        }
    }
}
