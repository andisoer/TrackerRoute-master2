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

    private String URL_POST_USERNAME = "https://sembarangsims.000webhostapp.com/backSims/select_absensi.php";
    private String URL_GET_KORDINAT = "https://sembarangsims.000webhostapp.com/backSims/select_koordinat.php";

    private String TAG_SUCCESS = "success";
    private String TAG_MESSAGE = "message";

    private String result = "";

    private int varUserUidNow;
    private String jsonResponse;

    SwipeRefreshLayout refreshLayout;

    TextView cobak, testKoordinat, awalTestKoordinat, akhirTestKoordinat;

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
        varUsernameNow = (sharedPreferences.getString(TAG_NAMA, ""));
        //cobak = findViewById(R.id.cobak);
        testKoordinat = findViewById(R.id.testKoordinat);
        awalTestKoordinat = findViewById(R.id.awalTestKoordinat);
        akhirTestKoordinat = findViewById(R.id.akhirTestKoordinat);

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

                params.put(TAG_kodeunik, varUsernameNow+kodeunik);

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
                            item.setAwal(obj.getString(TAG_KOORDINAT_AWAL));
                            item.setAkhir(obj.getString(TAG_KOORDINAT_AKHIR));
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

                Log.e(TAG, ""+params);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        /*
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_POST_USERNAME, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Log.e(TAG, response.toString());
                Log.i("tagconvertstr", "["+response.toString()+"]");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        ModelListAbsen item = new ModelListAbsen();

                        item.setUsername(obj.getString(TAG_USERNAME));
                        item.setAwal(obj.getString(TAG_KOORDINAT_AWAL));
                        item.setAkhir(obj.getString(TAG_KOORDINAT_AKHIR));
                        item.setDate(obj.getString(TAG_DATE));

                        listAbsen.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RekapAbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(RekapAbsenActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String>getParams(){
                Date currentDate = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                String tanggal = simpleDateFormat.format(currentDate);

                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_daTe, tanggal);

                Log.e(TAG, ""+params);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        /*
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_POST_USERNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response : " + response.toString());

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject object = dataArray.getJSONObject(i);

                        String username = object.getString(TAG_USERNAME);
                        String awal = object.getString(TAG_KOORDINAT_AWAL);
                        String akhir = object.getString(TAG_KOORDINAT_AKHIR);
                        String date = object.getString(TAG_DATE);

                        jsonResponse = "";
                        jsonResponse += "Username : " +username+"\n";
                        jsonResponse += "Awal : "+awal+"\n";
                        jsonResponse += "Akhir : "+akhir+"\n";
                        jsonResponse += "Tanggal : "+date+"\n";

                        cobak.setText(jsonResponse);

                        ModelListAbsen modelAbsen = new ModelListAbsen();
                        modelAbsen.setId(object.getString(TAG_ID));
                        modelAbsen.setUsername(object.getString(TAG_USERNAME));
                        modelAbsen.setKodeUnik(object.getString(TAG_UNIQUE_CODE));
                        modelAbsen.setLink_gambar(object.getString(TAG_LINK_IMAGE));
                        modelAbsen.setAwal(object.getString(TAG_KOORDINAT_AWAL));
                        modelAbsen.setAkhir(object.getString(TAG_KOORDINAT_AKHIR));
                        modelAbsen.setId_device(object.getString(TAG_ID_DEVICE));
                        modelAbsen.setDate(object.getString(TAG_DATE));

                        listAbsen.add(modelAbsen);

                        listAbsen.add(new ModelListAbsen(
                                object.getString(TAG_ID),
                                object.getString(TAG_USERNAME),
                                object.getString(TAG_UNIQUE_CODE),
                                object.getString(TAG_LINK_IMAGE),
                                object.getString(TAG_KOORDINAT_AWAL),
                                object.getString(TAG_KOORDINAT_AKHIR),
                                object.getString(TAG_ID_DEVICE),
                                object.getString(TAG_DATE)
                        ));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RekapAbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
        */
    }

    @Override
    public void onRefresh() {
        listAbsen.clear();
        adapter.notifyDataSetChanged();
        postUsername();
    }

    /*
    public class AdapterListAbsen2 extends RecyclerView.Adapter<AdapterListAbsen2.ViewHolder>{

        private List<ModelListAbsen> modelListAbsens;
        private Context context;

        public AdapterListAbsen2(Context context, List<ModelListAbsen> modelListAbsenArrayList) {
            this.modelListAbsens = modelListAbsenArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_absen, null);

            ViewHolder viewHolder = new ViewHolder(mView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ModelListAbsen mLA = modelListAbsens.get(position);

            holder.username.setText(String.valueOf(mLA.getUsername()));
            holder.tanggalAbsen.setText(String.valueOf(mLA.getDate()));
            holder.kordinatAwal.setText(String.valueOf(mLA.getAwal()));
            holder.kordinatAkhir.setText(String.valueOf(mLA.getAkhir()));
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView username, tanggalAbsen, kordinatAwal, kordinatAkhir;

            public ViewHolder(View itemView) {
                super(itemView);

                username = (TextView)itemView.findViewById(R.id.userName);
                tanggalAbsen = (TextView)itemView.findViewById(R.id.tanggalAbsen);
                kordinatAwal = (TextView)itemView.findViewById(R.id.awalKoordinat);
                kordinatAkhir = (TextView)itemView.findViewById(R.id.akhirKoordinat);
            }
        }
    }
    */
}

