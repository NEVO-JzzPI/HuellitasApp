package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/** Pantalla principal: lista en tiempo real las mascotas de la colección "mascotas". */
public class ListarActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private final List<Mascota> mascotas = new ArrayList<>();
    private MascotaAdapter adapter;
    private TextView tvVacio;

    private FirebaseFirestore db;
    private ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        db = FirebaseFirestore.getInstance();
        tvVacio = findViewById(R.id.tv_vacio);

        RecyclerView rv = findViewById(R.id.rv_mascotas);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MascotaAdapter(mascotas, m -> {
            // Se envía el ID del documento a VerActivity
            Intent intent = new Intent(this, VerActivity.class);
            intent.putExtra(EXTRA_ID, m.getId());
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_agregar);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AgregarActivity.class)));
    }

    // READ en tiempo real: la lista se actualiza sola ante cualquier cambio en Firestore
    @Override
    protected void onStart() {
        super.onStart();
        listener = db.collection("mascotas")
                .orderBy("nombre", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    mascotas.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            Mascota m = doc.toObject(Mascota.class);
                            if (m != null) {
                                m.setId(doc.getId());
                                mascotas.add(m);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    tvVacio.setVisibility(mascotas.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null) {
            listener.remove();
            listener = null;
        }
    }
}
