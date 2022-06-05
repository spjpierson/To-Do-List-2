package com.piersonapps.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView


class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
       val cal = findViewById<CalendarView>(R.id.calendar_view_calendar);
        cal.setOnDateChangeListener()
    }
}

private fun CalendarView.setOnDateChangeListener() {
    TODO("Not yet implemented")
}
