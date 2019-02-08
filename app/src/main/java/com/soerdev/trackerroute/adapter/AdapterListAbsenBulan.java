package com.soerdev.trackerroute.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soerdev.trackerroute.R;
import com.soerdev.trackerroute.model.ModelListAbsenBulan;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterListAbsenBulan extends BaseAdapter {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<ModelListAbsenBulan> data;

    public AdapterListAbsenBulan(Activity activity, List<ModelListAbsenBulan> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int location) {
        return data.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = layoutInflater.inflate(R.layout.list_absen_bulan, null);
        }

        TextView username = (TextView)view.findViewById(R.id.userName_Bulanan);
        TextView tanggalAbsen = (TextView)view.findViewById(R.id.tanggalAbsen_Bulanan);
        TextView awalWaktu = (TextView)view.findViewById(R.id.awalWaktuAbsen_Bulanan);
        TextView akhirWaktu = (TextView)view.findViewById(R.id.akhirWaktuAbsen_Bulanan);
        TextView awalKoordinatBulan = (TextView)view.findViewById(R.id.awalKoordinatBulan);
        TextView akhirKoordinatBulan = (TextView)view.findViewById(R.id.akhirKoordinatBulan);

        ModelListAbsenBulan modelListAbsenBulan = data.get(position);

        username.setText(modelListAbsenBulan.getUsername());
        tanggalAbsen.setText(modelListAbsenBulan.getDate());
        awalWaktu.setText("Waktu Masuk : "+modelListAbsenBulan.getWaktu_awal());
        akhirWaktu.setText("Waktu Akhir : "+modelListAbsenBulan.getWaktu_akhir());
        awalKoordinatBulan.setText(modelListAbsenBulan.getAwal());
        akhirKoordinatBulan.setText(modelListAbsenBulan.getAkhir());

        return view;
    }
}
