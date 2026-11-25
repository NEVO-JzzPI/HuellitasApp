package com.example.huellitasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

/** CREATE: registra una nueva mascota en la colección "mascotas". */
public class AgregarActivity extends AppCompatActivity {

    private FormularioMascota formulario;
    private Button btnGuardar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        db = FirebaseFirestore.getInstance();
        formulario = new FormularioMascota(this);
        btnGuardar = findViewById(R.id.btn_guardar);

        btnGuardar.setOnClickListener(v -> guardar());
        findViewById(R.id.btn_cancelar).setOnClickListener(v -> finish());
    }

    private void guardar() {
        if (!formulario.validar()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        btnGuardar.setEnabled(false);  // evita duplicados por doble toque
        db.collection("mascotas")
                .add(formulario.obtener())
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "Mascota registrada correctamente",
                            Toast.LENGTH_SHORT).show();
                    finish();   // vuelve al listado, que se actualiza solo
                })
                .addOnFailureListener(e -> {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(this, "No se pudo registrar: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}
