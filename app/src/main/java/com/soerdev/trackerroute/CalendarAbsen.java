package com.soerdev.trackerroute;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.CalendarView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.extensions.CalendarViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAbsen extends AppCompatActivity {

    private String STATUSABSEN = "masuk";

    List<EventDay> event = new ArrayList<EventDay>();

    ProgressDialog progressDialog;
    com.applandeo.materialcalendarview.CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_absen);

        Toolbar toolbar = findViewById(R.id.toolbarAbsensiCalendar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu Sebentar . . .");
        progressDialog.setCancelable(false);

        deklarasiKalender();
    }

    private void deklarasiKalender() {
        progressDialog.show();
        calendarView = findViewById(R.id.calendarView);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 1);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -6);

        calendarView.setMaximumDate(max);
        calendarView.setMinimumDate(min);

        loadingAbsen();
    }

    private void loadingAbsen() {
        for(int i = -50; i <= 10; i++) {
            if (STATUSABSEN.equals("masuk")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i);
                event.add(new EventDay(calendar, R.drawable.ic_action_check));
            } else if (STATUSABSEN.equals("alpha")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i);
                event.add(new EventDay(calendar, R.drawable.ic_action_warningyellow));
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i);
                event.add(new EventDay(calendar, R.drawable.ic_action_warning));
            }
        }
        calendarView.setEvents(event);
        progressDialog.dismiss();
    }
}
