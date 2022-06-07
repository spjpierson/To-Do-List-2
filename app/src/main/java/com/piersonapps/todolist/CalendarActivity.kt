package com.piersonapps.todolist

import android.app.*
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import com.piersonapps.todolist.R
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.app.NotificationManagerCompat
import android.content.Context as ContentContext
import android.graphics.Color
import android.icu.util.Calendar
import android.icu.util.Calendar.getInstance
import android.text.format.DateFormat
import com.google.android.material.timepicker.TimeFormat
import com.piersonapps.todolist.databinding.ActivityCalendarBinding
import layout.channelID
import layout.messageExtra
import layout.notificationID
import layout.titleExtra
import java.util.*


class CalendarActivity: AppCompatActivity(){
    private lateinit var  binding : ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        binding.calendarSubmitBtn.setOnClickListener { scheduleNotification() }
    }
    private fun scheduleNotification()
    {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = binding.calendarTitleEditText.text.toString()
        val message = binding.calendarMessageEditText.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }
    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Schedule")
            .setMessage(
                "Title: " + title + "\nMessage: " + message + "\nAt: " + dateFormat.format(date) + " "+ timeFormat.format(date))
            .setPositiveButton("Yes"){_,_->}
            .show()
    }
    private fun getTime(): Long
    {
        val minute = binding.calendarTimePicker.minute
        val hour = binding.calendarTimePicker.hour
        val day = binding.calendarDatePicker.dayOfMonth
        val month = binding.calendarDatePicker.month
        val year = binding.calendarDatePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }
    private fun createNotificationChannel()
    {
        val name = "Notification Channel"
        val desc = "TODO List Calendar Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificaitonManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificaitonManager.createNotificationChannel(channel)
    }
}