package com.example.androidbeginner.exampleservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.androidbeginner.MainActivity
import com.example.androidbeginner.R

class PlaybackService: Service() {

    private val CHANNEL_ID = "MyPlaybackServiceChannel"
    private val NOTIFICATION_ID = 123
    private val TAG = "PlaybackService"

    private lateinit var notificationManager: NotificationManager

    companion object {
        const val ACTION_PLAY = "play"
        const val ACTION_PAUSE = "pause"
        const val ACTION_STOP = "stop"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "MyForegoundServiceChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Playback service")
            .setContentText("Running playback service")
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .addAction(R.drawable.ic_play, "Play", PendingIntent.getBroadcast(this, 0, Intent(
                ACTION_PLAY), PendingIntent.FLAG_IMMUTABLE))
            .addAction(R.drawable.ic_pause, "Pause", PendingIntent.getBroadcast(this, 0, Intent(
                ACTION_PAUSE), PendingIntent.FLAG_IMMUTABLE))
            .addAction(R.drawable.ic_stop, "Stop", PendingIntent.getBroadcast(this, 0, Intent(
                ACTION_STOP), PendingIntent.FLAG_IMMUTABLE))
            .setOnlyAlertOnce(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        // Register a broadcast receiver to handle custom broadcast intents
        registerReceiver(notificationControlReceiver, IntentFilter().apply {
            addAction(ACTION_PLAY)
            addAction(ACTION_PAUSE)
            addAction(ACTION_STOP)
        }, RECEIVER_NOT_EXPORTED)

        // Do some work in the foreground service...


        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        unregisterReceiver(notificationControlReceiver)
        super.onDestroy()
    }

    private val notificationControlReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null) return
            Log.d(TAG, "Received action ${intent?.action}")

            when (intent?.action) {
                ACTION_PLAY -> {
                    // handle play button click
                    // ...
                    Log.d(TAG, "Action PLAY")
                }
                ACTION_PAUSE -> {
                    // handle pause button click
                    // ...
                    Log.d(TAG, "Action PAUSE")
                }
                ACTION_STOP -> {
                    // handle stop button click
                    // ...
                    Log.d(TAG, "Action STOP")
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}