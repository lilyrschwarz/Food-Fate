package edu.gwu.myapplication

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

class PizzaActivity : AppCompatActivity() {

    private lateinit var orderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pizza)
        orderButton = findViewById(R.id.orderbutton)

        createNotificationChannel()

        //make fux
        showNewNotification()

        /**AlertDialog.Builder(this)
            .setTitle("Didn't Like What You Made?")
            .setMessage("Order Domino's instead!!")
            .setPositiveButton("How convenient!") { dialog, which ->
                // User pressed OK
            }
            .show()**/

        //explicit intent to launch Domino's website to order online
        orderButton.setOnClickListener {
            Log.d("PizzaActivity", "Order Clicked")
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dominos.com/en/about-pizza/food-online/"))
            startActivity(intent)

        }
        }
    private fun showNewNotification()
    {

        val mBuilder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Welcome to Food Fate")
            .setContentText("Tutorial 3/3")
            .setProgress(100, 100, false)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Cooking isn't for everybody... Luckily delivery exists! Order your favorite pizza"
                    + " from our Order Activity."))

        NotificationManagerCompat.from(this).notify(0, mBuilder.build())

    }
    private fun createNotificationChannel() {
        // Needed for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Notifications"
            val descriptionText = "The app's default notification set"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("default", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    }
