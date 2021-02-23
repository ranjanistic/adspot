package org.ranjanistic.adspot

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import org.ranjanistic.adspot.databinding.ActivityMainBinding
import java.lang.reflect.Method
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    private val codes = Codes()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        context = this
        binding.askNotificationPermit.setOnClickListener {
            val am = (getSystemService(ACTIVITY_SERVICE) as ActivityManager)

            am.runningAppProcesses.forEach {
                Log.e("processes",it.pid.toString())
            }
            askNotifyPermit()
        }
    }

    private fun askNotifyPermit(){
        startActivityForResult(
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),
            codes.NOTIFICATION_PERMIT
        )
    }
    override fun onStart() {
        super.onStart()
        this.setNotificationPermitStatus()
        this.registerReceiver(this.mNotificationReceiver, IntentFilter(codes.actionNotification))
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(this.mNotificationReceiver)
    }
    private fun setNotificationPermitStatus(){
        binding.notificationStatus.text = "Notification permit: ${this.isNotificationServiceRunning()}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            codes.NOTIFICATION_PERMIT -> this.setNotificationPermitStatus()
        }
    }


    private fun setMainStatus(status: String? = "loading"){
        binding.status.text = status
    }

    private fun setActStatus(status: String? = "loading"){
        binding.actStatus.text = status
    }

    private fun killApp(packname: String?){
        this.setActStatus("Killing ${packname}")
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
       am.killBackgroundProcesses(packname)
        this.setActStatus("Maybe killed ${packname}? Starting")
        this.startApp(packname)
    }

    private fun startApp(packname: String?){
        this.setActStatus("Starting ${packname}")
        val pm:PackageManager = packageManager
        startActivity(pm.getLaunchIntentForPackage(packname!!))
    }


    private val mNotificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val key = intent.getStringExtra(codes.notificationID)
            val state = intent.getStringExtra(codes.notificationStatus)
            val from = intent.getStringExtra(codes.notificationAppName)
            val msg = intent.getStringExtra(codes.notificationTicker)
            val time = intent.getStringExtra(codes.notificationTime)
            val ongoing = intent.getBooleanExtra(codes.notificationOngoing, false)
            Log.e("msg",msg!!)
            setMainStatus(msg)
            if(msg.contains("advertisement")||msg.toLowerCase(Locale.ROOT)=="advertisement"){
                setActStatus("Ad detected!")
                killApp(intent.getStringExtra(codes.notificationPackage))
            }
        }
    }


    private fun isNotificationServiceRunning(): Boolean =
        NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)
}