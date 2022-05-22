package com.example.kelascsqllite.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kelascsqllite.MainActivity;
import com.example.kelascsqllite.R;
import com.example.kelascsqllite.app.AppController;
import com.example.kelascsqllite.database.DBcontroller;
import com.example.kelascsqllite.database.Teman;
import com.example.kelascsqllite.edit_teman;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
    private ArrayList<Teman> ListData;
    private Context control;

    public TemanAdapter(ArrayList<Teman> listData) {
        this.ListData = listData;
    }

    @Override
    public TemanAdapter.TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutinf = LayoutInflater.from(parent.getContext());
        View view = layoutinf.inflate(R.layout.row_data_teman,parent,false);
        control = parent.getContext();
        return new TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TemanAdapter.TemanViewHolder holder, int position) {
        String nma,tlp,id;

        id = ListData.get(position).getId();
        nma = ListData.get(position).getNama();
        tlp = ListData.get(position).getTelpon();
        DBcontroller db = new DBcontroller(control);

        holder.namaTxt.setTextColor(Color.BLUE);
        holder.namaTxt.setTextSize(20);
        holder.namaTxt.setText(nma);
        holder.telponTxt.setText(tlp);

        holder.cardku.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(control, holder.cardku);
                popupMenu.inflate(R.menu.menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem Item) {
                        switch (Item.getItemId()){
                            case R.id.edit:
                                Bundle bundle = new Bundle();

                                bundle.putString("key1", id);
                                bundle.putString("key2", nma);
                                bundle.putString("key3", tlp);

                                Intent intent = new Intent(view.getContext(), edit_teman.class);
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(view.getContext());
                                alertdb.setTitle("Yakin " + nma + " akan dihapus?");
                                alertdb.setMessage("Tekan Ya untuk menghapus");
                                alertdb.setCancelable(false);

                                alertdb.setPositiveButton("ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        HapusData(id);
                                        Toast.makeText(control, "", Toast.LENGTH_SHORT).show();Toast.makeText(view.getContext(), "Data " + id + " telah dihapus", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(view.getContext(), MainActivity.class);
                                        view.getContext().startActivity(intent1);
                                    }
                                });
                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog adlg = alertdb.create();
                                adlg.show();

                                break;

                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    public void HapusData(final String idx){
        String url_update = "http://10.0.2.2:8081/PAM/deletetm.php";
        final String TAG = MainActivity.class.getSimpleName();
        final String TAG_SUCCES = "success";
        final int[] sukses = new int[1];

        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon : " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    sukses[0] = jObj.getInt(TAG_SUCCES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Eror : " + error.getMessage());
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();

                params.put("id",idx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringReq);
    }

    @Override
    public int getItemCount() {

        return (ListData != null)?ListData.size() : 0;
    }

    public class TemanViewHolder extends RecyclerView.ViewHolder {

        private CardView cardku;
        private TextView namaTxt,telponTxt;
        public TemanViewHolder(View view) {
            super(view);
            cardku = (CardView) view.findViewById(R.id.recyclerView);
            namaTxt = (TextView) view.findViewById(R.id.textNama);
            telponTxt = (TextView) view.findViewById(R.id.textTelpon);
        }
    }

}
