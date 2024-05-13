package com.example.recycler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Kontak> data;
    private RecyclerView rvKontak;
    private KontakAdapter kontakAdapter;
    private SearchView searchView;
    private ImageView imageViewReceived;
    private Button sendImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String imageFilePath = getIntent().getStringExtra("imageFilePath");

        initData();
        initView();

        sendImageButton = findViewById(R.id.selectAllButton);
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImageToSelectedContacts();
            }
        });
    }

    private void initData() {
        data = new ArrayList<>();
        Kontak a = new Kontak("Yeri", R.drawable.yeri_avatar);
        Kontak b = new Kontak("Seunghan", R.drawable.seunghan_avatar);
        Kontak c = new Kontak("Wonbin", R.drawable.wonbin_avatar);
        Kontak d = new Kontak("Irene", R.drawable.irene_avatar);
        Kontak e = new Kontak("Natalia", R.drawable.natalia_avatar);
        Kontak f = new Kontak("Jay", R.drawable.jay_avatar);
        for (int i = 0; i < 5; i++) {
            data.add(a);
            data.add(b);
            data.add(c);
            data.add(d);
            data.add(e);
            data.add(f);
        }
    }

    private void initView() {
        rvKontak = findViewById(R.id.rvKontak);
        kontakAdapter = new KontakAdapter(ListActivity.this, data);
        RecyclerView.LayoutManager lm =
                new LinearLayoutManager(ListActivity.this,
                        LinearLayoutManager.VERTICAL, false);
        rvKontak.setLayoutManager(lm);
        rvKontak.setAdapter(kontakAdapter);
    }

    private void sendImageToSelectedContacts() {
        List<Kontak> selectedContacts = kontakAdapter.getSelectedContacts();

        for (Kontak kontak : selectedContacts) {
            sendImageToContact(kontak);
        }

    }

    private void sendImageToContact(Kontak kontak) {

        String imageFilePath = getIntent().getStringExtra("imageFilePath");
        Uri imageUri = Uri.parse(imageFilePath);
        String contactName = kontak.nama;
        Toast.makeText(this, "Mengirim gambar ke kontak " + contactName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }
}
