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

        // Mendapatkan imageFilePath dari intent
        String imageFilePath = getIntent().getStringExtra("imageFilePath");

        // Inisialisasi UI dan data kontak
        initData();
        initView();

        // Atur OnClickListener untuk tombol kirim
        sendImageButton = findViewById(R.id.selectAllButton);
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImageToSelectedContacts();
            }
        });
    }

    private void initData() {
        // Inisialisasi data kontak
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
        // Atur RecyclerView untuk menampilkan daftar kontak
        rvKontak = findViewById(R.id.rvKontak);
        kontakAdapter = new KontakAdapter(ListActivity.this, data);
        RecyclerView.LayoutManager lm =
                new LinearLayoutManager(ListActivity.this,
                        LinearLayoutManager.VERTICAL, false);
        rvKontak.setLayoutManager(lm);
        rvKontak.setAdapter(kontakAdapter);
    }

    private void sendImageToSelectedContacts() {
        // Mendapatkan kontak yang dipilih
        List<Kontak> selectedContacts = kontakAdapter.getSelectedContacts();

        // Mengirim gambar ke setiap kontak yang dipilih
        for (Kontak kontak : selectedContacts) {
            sendImageToContact(kontak);
        }

        // Jika ingin mengirim juga ke server, tambahkan logika disini
    }

    private void sendImageToContact(Kontak kontak) {
        // Mendapatkan URI gambar dari file imageFilePath
        String imageFilePath = getIntent().getStringExtra("imageFilePath");
        Uri imageUri = Uri.parse(imageFilePath);

        // Gunakan nama kontak untuk mengidentifikasi kontak yang akan menerima gambar
        String contactName = kontak.nama;
        // Implementasikan logika untuk mengirim gambar ke kontak dengan nama 'contactName'
        // Misalnya, Anda dapat menggunakan API atau layanan yang sesuai untuk mengirim pesan ke kontak dengan nama 'contactName'
        // ...
        // Untuk contoh, saya akan menampilkan Toast untuk menunjukkan bahwa gambar akan dikirim ke kontak dengan nama yang dipilih
        Toast.makeText(this, "Mengirim gambar ke kontak " + contactName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        // Implementasi jika ada View yang memerlukan onClickListener
    }
}
