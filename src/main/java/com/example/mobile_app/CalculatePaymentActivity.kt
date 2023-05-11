package com.example.mobile_app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class CalculatePaymentActivity : AppCompatActivity() {


    private lateinit var ticketInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var resultText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_payment)

        // Set up references to UI elements
        ticketInput = findViewById(R.id.ticket_input)
        priceInput = findViewById(R.id.price_input)
        resultText = findViewById(R.id.result_text)

        val calculateButton = findViewById<Button>(R.id.calculate_button)
        calculateButton.setOnClickListener {
            calculate()
        }
        val click = findViewById<Button>(R.id.next_button)
        click.setOnClickListener {

            val intentApply = Intent(this, BookTicket::class.java)
            startActivity(intentApply)
        }
    }
    private fun calculate() {
        // Get input values
        val tickets = ticketInput.text.toString().toIntOrNull() ?: 0
        val price = priceInput.text.toString().toDoubleOrNull() ?: 0.0

        // Calculate values
        val total = tickets * price

        // Update result text
        resultText.text = "No. of Tickets: $tickets\n\nAmount to Pay: $${"%.2f".format(total)}"
    }
}

