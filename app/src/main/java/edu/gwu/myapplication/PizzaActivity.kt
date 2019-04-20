package edu.gwu.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.net.Uri

class PizzaActivity : AppCompatActivity() {

    private lateinit var orderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pizza)
        orderButton = findViewById(R.id.orderbutton)

        AlertDialog.Builder(this)
            .setTitle("Didn't Like What You Made?")
            .setMessage("Order Domino's instead!!")
            .setPositiveButton("How convenient!") { dialog, which ->
                // User pressed OK
            }
            .show()

        //explicit intent to launch Domino's website to order online
        orderButton.setOnClickListener {
            Log.d("PizzaActivity", "Order Clicked")
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dominos.com/en/about-pizza/food-online/"))
            startActivity(intent)

        }
        }
    }
