package edu.gwu.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import edu.gwu.myapplication.R
import android.widget.Toast

class IngredientsActivity : AppCompatActivity()  {

    private lateinit var recipeButton: Button
    private lateinit var enter1: EditText
    private lateinit var enter2: EditText
    private lateinit var enter3: EditText


    private val textWatcher: TextWatcher = object : TextWatcher
    {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
            val inputIngredient1: String = enter1.text.toString().trim()
            val inputIngredient2: String = enter2.text.toString().trim()
            val inputIngredient3: String = enter3.text.toString().trim()
            val enableButton: Boolean = inputIngredient1.isNotEmpty() && inputIngredient2.isNotEmpty() && inputIngredient3.isNotEmpty()

            recipeButton.isEnabled = enableButton
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredients)

        Log.d("IngredientsActivity", "onCreate called")

        createNotificationChannel()

        /**make fux**/
        showNewNotification()

        recipeButton = findViewById(R.id.recipe_button)
        enter1 = findViewById(R.id.enter1)
        enter2 = findViewById(R.id.enter2)
        enter3 = findViewById(R.id.enter3)

        enter1.addTextChangedListener(textWatcher)
        enter2.addTextChangedListener(textWatcher)
        enter3.addTextChangedListener(textWatcher)

        recipeButton.setOnClickListener {
            Log.d("IngredientsActivity", "Recipe Clicked")
            val intent: Intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showNewNotification()
    {

        val mBuilder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Welcome to Food Fate")
            .setContentText("Tutorial 2/3")
            .setProgress(100, 67, false)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("So you've decided to cook? Good for you! Here simply enter 3 ingredients and our" +
                            " app will find some potential recipe options for you."))

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