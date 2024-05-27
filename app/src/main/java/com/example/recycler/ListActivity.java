package com.example.recycler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
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

        View backView = findViewById(R.id.VwBack);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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

        String imageFilePath = getIntent().getStringExtra("imageFilePath");
        if (imageFilePath != null && !imageFilePath.isEmpty()) {
            String uploadDate = "2024-05-26";
            String status = "sent";

            File imageFile = new File(imageFilePath);
            if (imageFile.exists()) {
                uploadImageToFirebase(new File(String.valueOf(imageFile)), uploadDate, status, selectedContacts);
            } else {
                Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Path file gambar tidak valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(File imageFile, String uploadDate, String status, List<Kontak> selectedContacts) {
        Uri fileUri = Uri.fromFile(imageFile);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + fileUri.getLastPathSegment());
        storageReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveImageToSQLite(imageFile.getAbsolutePath(), uploadDate, status);
                sendImageUrlToContacts(imageUrl, selectedContacts);
                Toast.makeText(ListActivity.this, "Gambar berhasil diunggah dan dikirim ke kontak terpilih", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(ListActivity.this, "Gagal mendapatkan URL gambar", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(ListActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
        });
    }

    private void sendImageUrlToContacts(String imageUrl, List<Kontak> selectedContacts) {
        String uploadDate = "2024-05-26";
        String status = "sent";
        for (Kontak kontak : selectedContacts) {
            saveImageUrlToFirebase(imageUrl, kontak, uploadDate, status);
        }
        for (Kontak kontak : selectedContacts) {
            sendImageToContact(kontak);
        }
    }

    private void sendImageToContact(Kontak kontak) {
        String imageFilePath = getIntent().getStringExtra("imageFilePath");
        Uri imageUri = Uri.parse(imageFilePath);
        String contactName = kontak.nama;
        Toast.makeText(this, "Mengirim gambar ke kontak " + contactName, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }




    private void saveImageUrlToFirebase(String imageUrl, Kontak kontak, String uploadDate, String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("contacts").child(kontak.getNama());
        String key = databaseReference.push().getKey();
        Image image = new Image();
        image.setId(Integer.parseInt(key));
        image.setFilePath(imageUrl);
        image.setUploadDate(uploadDate);
        image.setStatus(status);
        databaseReference.child(key).setValue(image)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ListActivity.this, "Berhasil menyimpan gambar dan informasi ke database", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ListActivity.this, "Gagal menyimpan informasi gambar ke database", Toast.LENGTH_SHORT).show();
                });
    }


    private void saveImageToSQLite(String imageFilePath, String uploadDate, String status) {
        Image image = new Image();
        image.setFilePath(imageFilePath);
        image.setUploadDate(uploadDate);
        image.setStatus(status);


        AppDatabase.getInstance(this).imageDao().insert(image);
    }


    @Override
    public void onClick(View v) {

    }
}
