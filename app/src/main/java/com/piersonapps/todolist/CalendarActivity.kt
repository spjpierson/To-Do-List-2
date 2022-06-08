package com.piersonapps.todolist

import android.app.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.icu.util.Calendar
import com.piersonapps.todolist.databinding.ActivityCalendarBinding
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
        val name = "Notif Channel"
        val desc = "TODO List Calendar Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificaitonManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificaitonManager.createNotificationChannel(channel)
    }

    companion object {
        lateinit var kt: Class<*>
    }
}