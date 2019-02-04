package com.soerdev.trackerroute;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CompactCalendar extends AppCompatActivity {

    TextView currentMonth;

    private SimpleDateFormat SDFcurrentMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    CompactCalendarView compactCalendarView;

    private String tanggalTest = "2019-02-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compact_calendar);

        Toolbar toolbar = findViewById(R.id.toolbarCompactCalendar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        currentMonth = findViewById(R.id.currentMonthXML);

        compactCalendarView = findViewById(R.id.compactCalendar);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        currentMonth.setText(SDFcurrentMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                currentMonth.setText(SDFcurrentMonth.format(firstDayOfNewMonth));
            }
        });

        cobakTest();
    }

    private void cobakTest() {
        try {
            
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggalTest);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tanggalFormatted = simpleDateFormat.format(date);

            Date date2 = simpleDateFormat.parse(tanggalFormatted);

            long epoch = date2.getTime();

            Event event = new Event(Color.RED, epoch);
            compactCalendarView.addEvent(event);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(CompactCalendar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}