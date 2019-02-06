package com.soerdev.trackerroute;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.soerdev.trackerroute.adapter.AdapterAbsenList;
import com.soerdev.trackerroute.app.AppController;
import com.soerdev.trackerroute.model.ModelKoordinat;
import com.soerdev.trackerroute.model.ModelListAbsen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RekapAbsenActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SharedPreferences sharedPreferences;

    List<ModelListAbsen> listAbsen = new ArrayList<ModelListAbsen>();
    //List<ModelKoordinat> listKoordinat = new ArrayList<ModelKoordinat>();

    List<String> arrayKoordinat = new ArrayList<String>();

    AdapterAbsenList adapter;
    Context context;

    private ListView listView;

    private String varUsernameNow;
    private String caritanggal;

    private String TAG = RekapAbsenActivity.class.getSimpleName();

    private String TAG_USERNAME = "username";
    private String TAG_ID = "id";
    private String TAG_UNIQUE_CODE = "kodeUnik";
    private String TAG_LINK_IMAGE = "link_gambar";
    private String TAG_KOORDINAT_AWAL = "awal";
    private String TAG_KOORDINAT_AKHIR = "akhir";
    private String TAG_KOORDINAT = "koordinat";
    private String TAG_NAMA = "nama";
    private String TAG_ID_DEVICE = "id_device";
    private String TAG_DATE = "date";
    private String TAG_daTe = "daTe";
    private String TAG_kodeunik = "kodeunik";
    private String TAG_nama = "nama";
    private String WAKTU_MASUK = "pklawal";
    private String WAKTU_KELUAR = "pklakhir";
    private String TAG_WAKTU_AWAL = "waktu_awal";
    private String TAG_WAKTU_AKHIR = "waktu_akhir";

    private String URL_POST_USERNAME = "https://sembarangsims.000webhostapp.com/backSims/select_absensi.php";
    private String URL_GET_KORDINAT = "https://sembarangsims.000webhostapp.com/backSims/select_koordinat.php";

    private String TAG_SUCCESS = "success";
    private String TAG_MESSAGE = "message";

    private String result = "";

    private int varUserUidNow;
    private String jsonResponse;

    SwipeRefreshLayout refreshLayout;

    TextView cobak, testKoordinat, awalTestKoordinat, akhirTestKoordinat, forToday;

    int success;

    String tag_json_obj = "json_obj_req";

    String outputKoordinat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_absen);

        Toolbar toolbar = findViewById(R.id.lihatRekapAbsen);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        caritanggal = getIntent().getExtras().getString("cari");

        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        varUserUidNow = (sharedPreferences.getInt(TAG_ID, 0));
        varUsernameNow = (sharedPreferences.getString(TAG_USERNAME, ""));
        //cobak = findViewById(R.id.cobak);
        testKoordinat = findViewById(R.id.testKoordinat);
        awalTestKoordinat = findViewById(R.id.awalTestKoordinat);
        akhirTestKoordinat = findViewById(R.id.akhirTestKoordinat);
        forToday = findViewById(R.id.forToday);

        forToday.setText("Rekapan Untuk "+caritanggal);

        refreshLayout = findViewById(R.id.swipe_rl);

        listView = findViewById(R.id.listAbsenRV);
        adapter = new AdapterAbsenList(RekapAbsenActivity.this, listAbsen);
        listView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(this);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                listAbsen.clear();
                adapter.notifyDataSetChanged();
                postUsername();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String kodeUnik = listAbsen.get(position).getKodeUnik();
                String koordinatAwal = listAbsen.get(position).getAwal();
                String koordinatAkhir = listAbsen.get(position).getAkhir();

                awalTestKoordinat.setText("");
                akhirTestKoordinat.setText("");
                testKoordinat.setText("");
                arrayKoordinat.clear();

                intenMapKoordinat(kodeUnik, koordinatAwal, koordinatAkhir);
            }
        });
    }

    private void intenMapKoordinat(String kodeUnik, String koordinatAwal, String koordinatAkhir) {
        String kodeunik = kodeUnik;
        String koorawal = koordinatAwal;
        String koorakhir = koordinatAkhir;
        ProgressDialog progressDialog = ProgressDialog.show(RekapAbsenActivity.this, null, "Mengambil Koordinat . . .", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_KORDINAT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            /*
                            ModelKoordinat itemKoordinat = new ModelKoordinat();

                            itemKoordinat.setKoordinat(obj.getString(TAG_KOORDINAT));

                            listKoordinat.add(itemKoordinat);
                            */

                            arrayKoordinat.add(obj.getString(TAG_KOORDINAT));

                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(RekapAbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    awalTestKoordinat.setText(koorawal);
                    akhirTestKoordinat.setText(koorakhir);

                    for (int i = 0; i < arrayKoordinat.size(); i++){
                        outputKoordinat += arrayKoordinat.get(i);
                    }

                    testKoordinat.setText(outputKoordinat);

                    String koorAwalGMAP = awalTestKoordinat.getText().toString();
                    String koorAkhirGMAP = akhirTestKoordinat.getText().toString();
                    String koordinatGMAP = testKoordinat.getText().toString();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            intentMap(koordinatGMAP, koorAwalGMAP, koorAkhirGMAP);
                            progressDialog.dismiss();
                        }
                    }, 2000);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RekapAbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                Toast.makeText(RekapAbsenActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String>getParams(){
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_kodeunik, kodeunik);

                Log.e(TAG, ""+params);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void intentMap(String koordinatGMAP, String koorawal, String koorakhir) {
        String koordinat = koordinatGMAP;
        String koorAwal = koorawal;
        String koorAkhir = koorakhir;

        if(koordinat.isEmpty()){

            Toast.makeText(RekapAbsenActivity.this, "Koordinat Kosong !", Toast.LENGTH_SHORT).show();
            /*
            Intent intent = new Intent(RekapAbsenActivity.this, CekKoordinat.class);
            intent.putExtra("koordinatAwal", koorAwal);
            intent.putExtra("koordinatAkhir", koorAkhir);
            intent.putExtra("daftarKoordinat", koordinat);
            startActivity(intent);

            Uri sembarang = Uri.parse("https://www.google.com/maps/dir/?api=1origin="+koorAwal+"&destination="+koorAkhir+"&waypoint="+koordinat+"&travelmode=" +
                    "driving");

            Intent intent = new Intent(Intent.ACTION_VIEW, sembarang);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
            */

        }else if(koorAwal.isEmpty()){
            Toast.makeText(RekapAbsenActivity.this, "Koordinat Awal Kosong !", Toast.LENGTH_SHORT).show();
        }else if(koorAkhir.isEmpty()){
            Toast.makeText(RekapAbsenActivity.this, "Koordinat Akhir Kosong !", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(RekapAbsenActivity.this, CekKoordinat.class);
            intent.putExtra("koordinatAwal", koorAwal);
            intent.putExtra("koordinatAkhir", koorAkhir);
            intent.putExtra("daftarKoordinat", koordinat);
            startActivity(intent);
        }
    }

    private void postUsername() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_POST_USERNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            ModelListAbsen item = new ModelListAbsen();

                            item.setUsername(obj.getString(TAG_USERNAME));
                            item.setWaktu_awal(obj.getString(TAG_WAKTU_AWAL));
                            item.setWaktu_akhir(obj.getString(TAG_WAKTU_AKHIR));
                            item.setDate(obj.getString(TAG_DATE));
                            item.setKodeUnik(obj.getString(TAG_UNIQUE_CODE));

                            listAbsen.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RekapAbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                } catch (JSONException f) {
                    f.printStackTrace();
                    Toast.makeText(RekapAbsenActivity.this, f.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                Toast.makeText(RekapAbsenActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String>getParams(){
                //Date currentDate = Calendar.getInstance().getTime();
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //String tanggal = simpleDateFormat.format(currentDate);

                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_daTe, caritanggal);
                params.put(TAG_nama, varUsernameNow);

                Log.e(TAG, ""+params);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onRefresh() {
        listAbsen.clear();
        adapter.notifyDataSetChanged();
        postUsername();
    }

}

