package edu.gwu.myapplication

import android.app.*
import android.content.Intent
import android.content.Context
import android.content.BroadcastReceiver
import android.app.NotificationManager

/**much of this section was guided by the following article regarding how to schedule notifications: https://stackoverflow.com/questions/36902667/how-to-schedule-notification-in-android**/
class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context:Context, intent:Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(notificationId, notification)
    }
    companion object {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }
}
