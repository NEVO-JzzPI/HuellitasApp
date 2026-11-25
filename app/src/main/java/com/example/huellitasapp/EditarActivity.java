package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

/** UPDATE: recibe el ID, carga los datos actuales y persiste los cambios con update(). */
public class EditarActivity extends AppCompatActivity {

    private FormularioMascota formulario;
    private Button btnGuardar;
    private FirebaseFirestore db;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        id = getIntent().getStringExtra(ListarActivity.EXTRA_ID);
        if (id == null) {
            Toast.makeText(this, "No se recibió el ID de la mascota", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        formulario = new FormularioMascota(this);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setEnabled(false);   // se habilita cuando terminan de cargar los datos

        btnGuardar.setOnClickListener(v -> guardarCambios());
        findViewById(R.id.btn_cancelar).setOnClickListener(v -> finish());

        cargarDatos();
    }

    private void cargarDatos() {
        db.collection("mascotas").document(id).get()
                .addOnSuccessListener(doc -> {
                    Mascota m = doc.toObject(Mascota.class);
                    if (m == null) {
                        Toast.makeText(this, "La mascota ya no existe", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    formulario.cargar(m);
                    btnGuardar.setEnabled(true);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    private void guardarCambios() {
        if (!formulario.validar()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Mascota m = formulario.obtener();
        btnGuardar.setEnabled(false);
        db.collection("mascotas").document(id)
                .update(
                        "nombre", m.getNombre(),
                        "especie", m.getEspecie(),
                        "edad", m.getEdad(),
                        "vacunado", m.isVacunado(),
                        "estado", m.getEstado(),
                        "descripcion", m.getDescripcion())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ListarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(this, "No se pudo actualizar: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}
