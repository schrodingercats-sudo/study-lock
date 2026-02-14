package com.studylock.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.studylock.app.MainActivity
import com.studylock.app.R

class TimerService : Service() {

    private val binder = TimerBinder()
    private var countDownTimer: CountDownTimer? = null
    private var timeRemaining = 0L
    private var timerListener: TimerListener? = null

    private val CHANNEL_ID = "TimerChannel"
    private val NOTIFICATION_ID = 1002

    interface TimerListener {
        fun onTick(timeRemaining: Long)
        fun onFinish()
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TIMER -> {
                val duration = intent.getLongExtra(EXTRA_DURATION, 25 * 60 * 1000L)
                startTimer(duration)
            }
            ACTION_PAUSE_TIMER -> pauseTimer()
            ACTION_RESUME_TIMER -> resumeTimer()
            ACTION_STOP_TIMER -> stopTimer()
        }
        return START_NOT_STICKY
    }

    fun setTimerListener(listener: TimerListener?) {
        this.timerListener = listener
    }

    private fun startTimer(durationMillis: Long) {
        timeRemaining = durationMillis
        startForeground(NOTIFICATION_ID, createNotification())

        countDownTimer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                timerListener?.onTick(millisUntilFinished)
                updateNotification()
            }

            override fun onFinish() {
                timeRemaining = 0
                timerListener?.onFinish()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }.start()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        timerListener?.onTick(timeRemaining)
    }

    private fun resumeTimer() {
        if (timeRemaining > 0) {
            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = millisUntilFinished
                    timerListener?.onTick(millisUntilFinished)
                    updateNotification()
                }

                override fun onFinish() {
                    timeRemaining = 0
                    timerListener?.onFinish()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            }.start()
        }
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    fun getTimeRemaining(): Long = timeRemaining

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Pomodoro Timer"
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
            .setContentTitle("Study Timer")
            .setContentText(formatTime(timeRemaining))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification() {
        val notification = createNotification()
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun formatTime(millis: Long): String {
        val minutes = millis / 1000 / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }

    companion object {
        const val ACTION_START_TIMER = "com.studylock.app.ACTION_START_TIMER"
        const val ACTION_PAUSE_TIMER = "com.studylock.app.ACTION_PAUSE_TIMER"
        const val ACTION_RESUME_TIMER = "com.studylock.app.ACTION_RESUME_TIMER"
        const val ACTION_STOP_TIMER = "com.studylock.app.ACTION_STOP_TIMER"
        const val EXTRA_DURATION = "duration"

        fun startTimer(context: Context, durationMillis: Long) {
            val intent = Intent(context, TimerService::class.java).apply {
                action = ACTION_START_TIMER
                putExtra(EXTRA_DURATION, durationMillis)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun pauseTimer(context: Context) {
            val intent = Intent(context, TimerService::class.java).apply {
                action = ACTION_PAUSE_TIMER
            }
            context.startService(intent)
        }

        fun resumeTimer(context: Context) {
            val intent = Intent(context, TimerService::class.java).apply {
                action = ACTION_RESUME_TIMER
            }
            context.startService(intent)
        }

        fun stopTimer(context: Context) {
            val intent = Intent(context, TimerService::class.java).apply {
                action = ACTION_STOP_TIMER
            }
            context.startService(intent)
        }
    }
}
