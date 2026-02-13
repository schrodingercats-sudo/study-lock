package com.studylock.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.studylock.app.MainActivity
import com.studylock.app.R

class LockScreenService : Service() {

    private var windowManager: WindowManager? = null
    private var lockOverlay: View? = null
    private val CHANNEL_ID = "StudyLockChannel"
    private val NOTIFICATION_ID = 1001

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_LOCK -> startLockOverlay()
            ACTION_STOP_LOCK -> stopLockOverlay()
        }
        return START_STICKY
    }

    private fun startLockOverlay() {
        if (lockOverlay != null) return

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lockOverlay = LayoutInflater.from(this).inflate(R.layout.lock_screen_overlay, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            -3
        )

        params.gravity = Gravity.CENTER
        windowManager?.addView(lockOverlay, params)
    }

    private fun stopLockOverlay() {
        lockOverlay?.let {
            windowManager?.removeView(it)
            lockOverlay = null
        }
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Study Lock Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Keeps the study lock active"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Study Mode Active")
            .setContentText("Complete your session to unlock your phone")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        stopLockOverlay()
        super.onDestroy()
    }

    companion object {
        const val ACTION_START_LOCK = "com.studylock.app.ACTION_START_LOCK"
        const val ACTION_STOP_LOCK = "com.studylock.app.ACTION_STOP_LOCK"

        fun startLock(context: Context) {
            val intent = Intent(context, LockScreenService::class.java).apply {
                action = ACTION_START_LOCK
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopLock(context: Context) {
            val intent = Intent(context, LockScreenService::class.java).apply {
                action = ACTION_STOP_LOCK
            }
            context.startService(intent)
        }
    }
}
