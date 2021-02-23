package org.ranjanistic.adspot

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast

class NotificationWatcher: NotificationListenerService() {
    private val codes = Codes()
    private val appName = "Spotify"
    private val appPackName = "com.spotify.music"
    override fun onListenerConnected() {
        super.onListenerConnected()
        activeNotifications.iterator().forEach {
            if(it.packageName == appPackName){
                sendBroadcast(
                    Intent(codes.actionNotification)
                        .putExtra(codes.notificationID, it.key)
                        .putExtra(codes.notificationStatus, codes.notificationPosted)
                        .putExtra(codes.notificationTime, it.postTime.toString())
                        .putExtra(codes.notificationPackage, it.packageName)
                        .putExtra(
                            codes.notificationAppName,
                            appName
                        )
                        .putExtra(
                            codes.notificationTicker,  it.notification.tickerText
                        )
                        .putExtra(codes.notificationOngoing, it.isOngoing)
                )
            }
        }
        Toast.makeText(applicationContext,"${resources.getString( R.string.app_name)} connected",Toast.LENGTH_LONG).show()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Toast.makeText(applicationContext,"${resources.getString( R.string.app_name)} disconnected",Toast.LENGTH_LONG).show()
    }
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if(sbn.packageName == appPackName) {
            sendBroadcast(
                Intent(codes.actionNotification)
                    .putExtra(codes.notificationID, sbn.key)
                    .putExtra(codes.notificationStatus, codes.notificationPosted)
                    .putExtra(codes.notificationTime, sbn.postTime.toString())
                    .putExtra(codes.notificationPackage, sbn.packageName)
                    .putExtra(
                        codes.notificationAppName,
                        appName
                    )
                    .putExtra(
                        codes.notificationTicker,  sbn.notification.tickerText
                    )
                    .putExtra(codes.notificationOngoing, sbn.isOngoing)
            )
        }
    }
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        if(sbn.packageName == appPackName) {
            sendBroadcast(
                Intent(codes.actionNotification)
                    .putExtra(codes.notificationID, sbn.key)
                    .putExtra(codes.notificationStatus, codes.notificationRemoved)
                    .putExtra(codes.notificationPackage, sbn.packageName)
                    .putExtra(codes.notificationAppName, appName)
                    .putExtra(codes.notificationTicker, "${sbn.notification.tickerText}")
            )
        }
    }
}