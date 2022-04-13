package com.example.kelascsqllite;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kelascsqllite.adapter.TemanAdapter;
import com.example.kelascsqllite.database.DBcontroller;
import com.example.kelascsqllite.database.Teman;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TemanAdapter adapter;
    private ArrayList<Teman> temanArrayList;
    DBcontroller controler = new DBcontroller( this);
    String id,nm,tlp;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingBtn);
        BacaData();
        adapter = new TemanAdapter(temanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TemanBaru.class);
                startActivity(intent);
            }
        });

    }

    public  void BacaData(){
        ArrayList<HashMap<String,String>> daftarTeman = controler.getAllTeman();
        temanArrayList = new ArrayList<>();
        //memindah dari hasil query kedalam teman
        for(int i=0;i<daftarTeman.size();i++){
            Teman teman = new Teman();

            teman.setId(daftarTeman.get(i).get("id").toString());
            teman.setNama(daftarTeman.get(i).get("nama").toString());
            teman.setTelpon(daftarTeman.get(i).get("telpon").toString());

            // Pindahkan dari Teman kedalam ArrayList teman di adapter
            temanArrayList.add(teman);
        }

    }
}