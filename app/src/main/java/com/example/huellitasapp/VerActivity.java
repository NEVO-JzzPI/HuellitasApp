package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

/** Detalle: recibe el ID del documento, lo consulta y permite editar o eliminar. */
public class VerActivity extends AppCompatActivity {

    private TextView tvNombre, tvEspecie, tvEdad, tvVacunado, tvEstado, tvDescripcion;
    private FirebaseFirestore db;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        id = getIntent().getStringExtra(ListarActivity.EXTRA_ID);
        if (id == null) {
            Toast.makeText(this, "No se recibió el ID de la mascota", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        tvNombre = findViewById(R.id.tv_nombre);
        tvEspecie = findViewById(R.id.tv_especie);
        tvEdad = findViewById(R.id.tv_edad);
        tvVacunado = findViewById(R.id.tv_vacunado);
        tvEstado = findViewById(R.id.tv_estado);
        tvDescripcion = findViewById(R.id.tv_descripcion);

        findViewById(R.id.btn_editar).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarActivity.class);
            intent.putExtra(ListarActivity.EXTRA_ID, id);
            startActivity(intent);
        });
        findViewById(R.id.btn_eliminar).setOnClickListener(v -> confirmarEliminar());
    }

    // Se consulta en onStart para refrescar los datos al volver de EditarActivity
    @Override
    protected void onStart() {
        super.onStart();
        if (id != null) {
            cargarMascota();
        }
    }

    private void cargarMascota() {
        db.collection("mascotas").document(id).get()
                .addOnSuccessListener(doc -> {
                    Mascota m = doc.toObject(Mascota.class);
                    if (m == null) {
                        Toast.makeText(this, "La mascota ya no existe", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    tvNombre.setText(m.getNombre());
                    tvEspecie.setText(m.getEspecie());
                    tvEdad.setText(m.getEdad() + (m.getEdad() == 1 ? " año" : " años"));
                    tvVacunado.setText(m.isVacunado() ? "Sí" : "No");
                    tvEstado.setText(m.getEstado());
                    tvDescripcion.setText(m.getDescripcion());
                })
                .addOnFailureListener(e -> Toast.makeText(this,
                        "Error al cargar: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    // DELETE con confirmación previa
    private void confirmarEliminar() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar mascota")
                .setMessage("¿Seguro que deseas eliminar este registro? "
                        + "Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminar())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminar() {
        db.collection("mascotas").document(id).delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                    irAlListado();
                })
                .addOnFailureListener(e -> Toast.makeText(this,
                        "No se pudo eliminar: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void irAlListado() {
        Intent intent = new Intent(this, ListarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
