package edu.gwu.myapplication.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import edu.gwu.myapplication.IngredientsActivity
import edu.gwu.myapplication.R
import android.app.AlertDialog
import android.widget.*
import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import edu.gwu.myapplication.PizzaActivity
import android.app.Activity
import android.app.AlarmManager
import android.content.SharedPreferences
import android.content.res.Configuration
import edu.gwu.myapplication.MyNotificationPublisher
import java.util.*
import android.os.SystemClock
import android.view.View


class MainActivity : AppCompatActivity() {

    /*
    * These variables are "lateinit" because can't actually assign a value to them until
    * onCreate() is called (e.g. we are promising to the Kotlin compiler that these will be
    * "initialized later").
    *
    * Alternative is to make them nullable and set them equal to null, but that's not as nice to
    * work with.
    *   private var username: EditText? = null
    */

    private lateinit var username: EditText

    private lateinit var password: EditText

    private lateinit var login: Button

    private lateinit var signUp: Button

    private lateinit var progressBar: ProgressBar

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var remember: CheckBox

    private lateinit var checkedBox: CheckBox

    var bool: Boolean = false

    //language button
    private lateinit var languageButton : Button







    /**
     * We're creating an "anonymous class" here (e.g. we're creating a class which implements
     * TextWatcher, but not creating an explicit class).
     *
     * object : TextWatcher == "creating a new object which implements TextWatcher"
     */
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // We're calling .getText() here, but in Kotlin you can omit the "get" or "set"
            // on a getter / setter and "pretend" you're using an actual variable.
            //      username.getText() == username.text
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            val enableButton: Boolean = inputtedUsername.isNotEmpty() && inputtedPassword.isNotEmpty()

            // Like above, this is really doing login.setEnabled(enableButton) under the hood
            login.isEnabled = enableButton
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**language change option**/
        languageButton = findViewById(R.id.mChangeLang) // look back and fix

        languageButton.setOnClickListener{
            showChangeLang()
        }


        /**notifications**/
        createNotificationChannel()

        firebaseAuth = FirebaseAuth.getInstance()

        signUp = findViewById(R.id.signUp)

        checkedBox = findViewById(R.id.remember);




        Log.d("MainActivity", "onCreate called")

        /**optimization: have shared preferences remember when it is a fux for welcome notification**/
        showNewUserNotification()

        //set to 1 minute for testing purposes
        scheduleNotification(this,  60000, 0)


        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)


        //when Check Box enabled, the destination entered is saved
        checkedBox.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("MainActivity", "Check Box Clicked")
            getData()
            if(isChecked){
                saveData()
                getData()
            }

        }


        signUp.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()

            val inputtedUsername2: String = username.text.toString().trim()
            val inputtedPassword2: String = password.text.toString().trim()

            if((inputtedUsername==inputtedUsername2) && (inputtedPassword==inputtedPassword2)){
                firebaseAuth.createUserWithEmailAndPassword(
                    inputtedUsername,
                    inputtedPassword
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If Sign Up is successful, Firebase automatically logs
                        // in as that user too (e.g. currentUser is set)
                        val currentUser: FirebaseUser? = firebaseAuth.currentUser
                        Toast.makeText(
                            this,
                            "Registered as: ${currentUser!!.email}",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        val exception = task.exception
                        Toast.makeText(
                            this,
                            "Failed to register: $exception",
                            Toast.LENGTH_LONG
                        ).show()
                        AlertDialog.Builder(this)
                            .setTitle("Oops!")
                            .setMessage("We couldn't match your gmail address & password with an existing google account. " +
                                    "Please check your information and try again!")
                            .setPositiveButton("Got It!") { dialog, which ->
                                // User pressed OK
                            }
                            .show()
                    }
                }

            }



        }




        // This is similar to the TextWatcher -- setOnClickListener takes a View.OnClickListener
        // as a parameter, which is an **interface with only one method**, so in this special case
        // you can just use a lambda (e.g. just open brances) instead of doing
        //      object : View.OnClickListener { ... }
        login.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(
                inputtedUsername,
                inputtedPassword
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser: FirebaseUser? = firebaseAuth.currentUser
                    Toast.makeText(
                        this,
                        "Logged in as: ${currentUser!!.email}",
                        Toast.LENGTH_LONG
                    ).show()

                    // User logged in, advance to the next screen

                    //use this for when i actually have an intent
                   val intent: Intent = Intent(this, IngredientsActivity::class.java)
                   startActivity(intent)
                } else {
                    val exception = task.exception
                    Toast.makeText(
                        this,
                        "Failed to login: $exception",
                        Toast.LENGTH_LONG
                    ).show()
                    AlertDialog.Builder(this)
                        .setTitle("Oops!")
                        .setMessage("Unfortunately there was an error with your Log In! Please remember to sign up" +
                                " before attempting to log in or check your spelling!")
                        .setPositiveButton("Got It!") { dialog, which ->
                            // User pressed OK
                        }
                        .show()
                }
            }


        }



    }

    /** function that has a dialog box to select language box & call the language change**/
    private fun showChangeLang(){

        val listItems = arrayOf("English", "中文")

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItems, -1){ dialog, which ->
            if (which == 0){
                setLocate("en")
                recreate()
            }
            else if (which == 1){
                setLocate("zh")
                recreate()
            }
            dialog.dismiss()

        }

        val mDialog = mBuilder.create()
        mDialog.show()

    }

    /**assistance with code from a helpful youtube tutorial: https://www.youtube.com/watch?v=xxPzi2h0Vvc **/
    /** setLocate function for changing language**/
    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config= Configuration()
        config.locale =locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()

    }

    fun saveData()
    {
        bool = true
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        sharedPref.edit().putString("SAVED_DESTINATION", username.text.toString()).apply()
        sharedPref.edit().putString("SAVED_DESTINATION", password.text.toString()).apply()
        sharedPref.edit().putBoolean("SAVED_BOOLEAN", bool).apply()
        Log.d("MainActivity", "Username and Password Saved!")
    }
    fun getData() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val saved = sharedPref.getString("SAVED_DESTINATION", "")
        if (saved.isNotEmpty()) {
            Toast.makeText(this, "Saved credentials!", Toast.LENGTH_LONG).show()
        }
    }

    /**Optimization Goal: create a separate class for notifications management**/
    private fun showNewUserNotification()
    {

        /**skip the cooking? just order pizza!**/
        val intent: Intent = Intent(this, PizzaActivity::class.java)
        val pendingIntentBuilder = TaskStackBuilder.create(this)
        pendingIntentBuilder.addNextIntentWithParentStack(intent)

        val pendingIntent = pendingIntentBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val mBuilder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Welcome to Food Fate")
            .setContentText("Tutorial 1/3")
            .setProgress(100, 33, false)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("To get started, register and login to the app with a valid GMail account." +
                        " Enter in 3 ingredients of your choice and start cooking! If you're not the cooking" +
                        " type, feel free to just order some Domino's pizza instead!"))
            .setContentIntent(pendingIntent)
            .addAction(0, "Order Pizza", pendingIntent)

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
    /**much of this section was guided by the following article regarding how to schedule notifications: https://stackoverflow.com/questions/36902667/how-to-schedule-notification-in-android
     * as well as with help from Nick via slack.
     * LIMITATION NOTE: must be run on an android less than 8.0**/
    private fun scheduleNotification(context:Context, delay:Long, notificationId:Int) {



        // Building the notification that will be shown after the alarm expires
        val builder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Food Fate")
            .setContentText("We Miss You!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Log back into Food Fate to start finding recipes!"))

        // Tapping on the notification will launch the 1st activity
        val launchIntent = Intent(this, MainActivity::class.java)
        val activityIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setContentIntent(activityIntent)
        builder.addAction(0, "Go to Food Fate", activityIntent)

        val notification = builder.build()

        // ------------------------------------------------

        // Now, build the intent that will be fired by the AlarmManager
        // e.g. this intent will trigger "wake up" your BroadcastReceiver, which will then show the previous
        // notification
        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)

        val alarmIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val totalTime = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, totalTime, alarmIntent)
    }

}



/*
    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called")
    }
}
*/


