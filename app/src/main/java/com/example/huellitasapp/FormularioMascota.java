package com.example.huellitasapp;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Encapsula el formulario compartido (layout form_mascota.xml) que usan
 * AgregarActivity y EditarActivity: validación, lectura y carga de datos.
 */
public class FormularioMascota {

    private final TextInputLayout tilNombre, tilEdad, tilDescripcion;
    private final TextInputEditText etNombre, etEdad, etDescripcion;
    private final Spinner spEspecie, spEstado;
    private final CheckBox cbVacunado;
    private final ArrayAdapter<CharSequence> especieAdapter, estadoAdapter;

    public FormularioMascota(Activity a) {
        tilNombre = a.findViewById(R.id.til_nombre);
        tilEdad = a.findViewById(R.id.til_edad);
        tilDescripcion = a.findViewById(R.id.til_descripcion);
        etNombre = a.findViewById(R.id.et_nombre);
        etEdad = a.findViewById(R.id.et_edad);
        etDescripcion = a.findViewById(R.id.et_descripcion);
        spEspecie = a.findViewById(R.id.sp_especie);
        spEstado = a.findViewById(R.id.sp_estado);
        cbVacunado = a.findViewById(R.id.cb_vacunado);

        especieAdapter = ArrayAdapter.createFromResource(a, R.array.especies,
                android.R.layout.simple_spinner_item);
        especieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEspecie.setAdapter(especieAdapter);

        estadoAdapter = ArrayAdapter.createFromResource(a, R.array.estados,
                android.R.layout.simple_spinner_item);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(estadoAdapter);
    }

    /** Valida los campos obligatorios y muestra el error en cada campo inválido. */
    public boolean validar() {
        boolean ok = true;
        tilNombre.setError(null);
        tilEdad.setError(null);
        tilDescripcion.setError(null);

        if (TextUtils.isEmpty(texto(etNombre))) {
            tilNombre.setError("El nombre es obligatorio");
            ok = false;
        }

        String edadTxt = texto(etEdad);
        if (TextUtils.isEmpty(edadTxt)) {
            tilEdad.setError("La edad es obligatoria");
            ok = false;
        } else {
            try {
                if (Integer.parseInt(edadTxt) < 0) {
                    tilEdad.setError("La edad no puede ser negativa");
                    ok = false;
                }
            } catch (NumberFormatException e) {
                tilEdad.setError("Ingresa una edad válida (número entero)");
                ok = false;
            }
        }

        if (TextUtils.isEmpty(texto(etDescripcion))) {
            tilDescripcion.setError("La descripción es obligatoria");
            ok = false;
        }
        return ok;
    }

    /** Construye una Mascota con los datos actuales del formulario (llamar tras validar()). */
    public Mascota obtener() {
        return new Mascota(
                texto(etNombre),
                (String) spEspecie.getSelectedItem(),
                Integer.parseInt(texto(etEdad)),
                cbVacunado.isChecked(),
                (String) spEstado.getSelectedItem(),
                texto(etDescripcion));
    }

    /** Carga los datos de una mascota en el formulario (usado al editar). */
    public void cargar(Mascota m) {
        etNombre.setText(m.getNombre());
        etEdad.setText(String.valueOf(m.getEdad()));
        etDescripcion.setText(m.getDescripcion());
        cbVacunado.setChecked(m.isVacunado());

        int posEspecie = especieAdapter.getPosition(m.getEspecie());
        if (posEspecie >= 0) spEspecie.setSelection(posEspecie);
        int posEstado = estadoAdapter.getPosition(m.getEstado());
        if (posEstado >= 0) spEstado.setSelection(posEstado);
    }

    private static String texto(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }
}
