package edu.gwu.myapplication.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import edu.gwu.myapplication.R
import kotlinx.android.synthetic.main.recipe_activity.*

class RecipeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_activity)

        createNotificationChannel()

        showNewNotification()

        /**interface to enter the three ingredients**/
        searchButton.setOnClickListener {

            var intent = Intent(this, RecipeList::class.java)
            var ingredient1 = Ingredient1.text.toString().trim()
            var ingredient2 = Ingredient2.text.toString().trim()
            var ingredient3 = Ingredient2.text.toString().trim()

            intent.putExtra("ingredient1", ingredient1)
            intent.putExtra("ingredient2", ingredient2)
            intent.putExtra("ingredient3", ingredient3)
            startActivity(intent)

        }
    }


    private fun showNewNotification()
    {

        val mBuilder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Welcome to Food Fate")
            .setContentText("Tutorial 2/3")
            .setProgress(100, 66, false)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Enter 3 Ingredients and find your new favorite recipe!"))

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
