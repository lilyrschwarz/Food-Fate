package edu.gwu.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import edu.gwu.myapplication.ui.main.MainActivity

class PizzaActivity : AppCompatActivity() {

    private lateinit var orderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pizza)
        orderButton = findViewById(R.id.orderbutton)

        orderButton.setOnClickListener {
            Log.d("PizzaActivity", "Order Clicked")
            /*
            Place holder until explicit intent is implemented
             */
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}