package com.soerdev.trackerroute;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

public class CekKoordinat extends AppCompatActivity {

    TextView awalKoordinat, akhirKoordinat, semuaKoordinat;

    Button btncheckKoordinat;

    String varAwalKoordinat, varAkhirKoordinat, varSemuaKoordinat, varAwalKoordinat1, varAkhirKoordinat1, varSemuaKoordinat1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_koordinat);

        awalKoordinat = findViewById(R.id.koordinatAwalCheck);
        akhirKoordinat = findViewById(R.id.koordinatAkhirCheck);
        semuaKoordinat = findViewById(R.id.koordinatSemuaCheck);

        btncheckKoordinat = findViewById(R.id.cekMap);

        Toolbar toolbar = findViewById(R.id.toolbarCekKoordinat);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        varAwalKoordinat = getIntent().getExtras().getString("koordinatAwal");
        varAkhirKoordinat = getIntent().getExtras().getString("koordinatAkhir");
        varSemuaKoordinat = getIntent().getExtras().getString("daftarKoordinat");

        awalKoordinat.setText(varAwalKoordinat);
        akhirKoordinat.setText(varAkhirKoordinat);
        semuaKoordinat.setText(varSemuaKoordinat);

        varAkhirKoordinat1 = akhirKoordinat.getText().toString();
        varAwalKoordinat1 = awalKoordinat.getText().toString();
        varSemuaKoordinat1 = semuaKoordinat.getText().toString();


                btncheckKoordinat.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View view) {
                intentMap();
            }
        });
    }

    private void intentMap() {
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+varAwalKoordinat1+"&destination=" +varAkhirKoordinat1
                +"&waypoints="+varSemuaKoordinat1+"&travelmode=driving");
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(CekKoordinat.this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }
}


/*
tetes air mata, basahi pipiku, disaat kita kan berpisah....
terucapkan janji, pada mu kasihku.,
takkan kulupakan dirimu,
begitu beratnya....
kau lepas diriku..
sebut namaku jika kau rindukan aku..
aku akan datang...


penyesalanku semakin dalam dan sedihh..
tlah kuserahkan semua milik dan hidupku...
aku takmau menderita begini...
mudah mudah an ini hanya mimpi...
hanya mimpii...

lagu luar*
++++
kau sayang selalu ku jaga...
takkan kulepas selamanya...
hilangkanlah..keraguanmuu. pada dirikuu.. di saat...
kau jauh dariku

biarkan ku mencoba, menjadi milik mu jangn tutup dirimu....

salah kah diri ini yang mencintaimu.. jangan tutup dirimu...

huuu huuu.. huuuu....



*/