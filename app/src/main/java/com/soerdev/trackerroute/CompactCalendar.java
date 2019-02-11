package com.soerdev.trackerroute;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.soerdev.trackerroute.app.AppController;
import com.soerdev.trackerroute.model.ModelKalendarAbsen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CompactCalendar extends AppCompatActivity {

    TextView currentMonth;

    SharedPreferences sharedPreferences;

    List<ModelKalendarAbsen> absenList = new ArrayList<ModelKalendarAbsen>();

    private SimpleDateFormat SDFcurrentMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    private SimpleDateFormat SDFonClickDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    CompactCalendarView compactCalendarView;

    private String tanggalTest = "2019-02-01";
    private String getTanggal;
    private String TAG_username = "username";
    private String TAG_USERNAME = "userName";
    private String TAG_DATE = "date";
    private String varUsernameNow;
    private String onClickTanggalCalendar;

    private String URL_POST = "https://sembarangsims.000webhostapp.com/backSims/select_date.php";

    private String TAG = CompactCalendar.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compact_calendar);

        Toolbar toolbar = findViewById(R.id.toolbarCompactCalendar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        varUsernameNow = (sharedPreferences.getString(TAG_username, ""));

        currentMonth = findViewById(R.id.currentMonthXML);

        compactCalendarView = findViewById(R.id.compactCalendar);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        currentMonth.setText(SDFcurrentMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                onClickTanggalCalendar = SDFonClickDay.format(dateClicked);
                Toast.makeText(CompactCalendar.this, onClickTanggalCalendar, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CompactCalendar.this, RekapAbsenActivity.class);
                intent.putExtra("cari", onClickTanggalCalendar);
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                currentMonth.setText(SDFcurrentMonth.format(firstDayOfNewMonth));
            }
        });


        loadDatafromDatabase();

        //cobakTest();
    }

    private void loadDatafromDatabase() {
        ProgressDialog progressDialog = ProgressDialog.show(CompactCalendar.this, null, "Mengambil Data . . .", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);

                try{
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            ModelKalendarAbsen item = new ModelKalendarAbsen();

                            //item.setUserName(obj.getString(TAG_USERNAME));
                            item.setTanggalAbsen(obj.getString(TAG_DATE));

                            getTanggal = obj.getString(TAG_DATE);

                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(getTanggal);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String tanggalFormatted = simpleDateFormat.format(date);

                                Date date2 = simpleDateFormat.parse(tanggalFormatted);

                                long epoch = date2.getTime();

                                Event event = new Event(Color.GREEN, epoch);
                                compactCalendarView.addEvent(event);

                            } catch (ParseException e) {
                                e.printStackTrace();
                                Toast.makeText(CompactCalendar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            absenList.add(item);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CompactCalendar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    progressDialog.dismiss();

                } catch (JSONException f) {
                    f.printStackTrace();
                    Toast.makeText(CompactCalendar.this, f.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                Toast.makeText(CompactCalendar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String>getParams(){
                Map<String, String>params = new HashMap<String, String>();

                params.put(TAG_USERNAME, varUsernameNow);

                Log.e(TAG, ""+params);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    /*
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
    */
}