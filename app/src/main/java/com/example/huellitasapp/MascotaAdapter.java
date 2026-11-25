package com.example.huellitasapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Mascota mascota);
    }

    private final List<Mascota> mascotas;
    private final OnItemClickListener listener;

    public MascotaAdapter(List<Mascota> mascotas, OnItemClickListener listener) {
        this.mascotas = mascotas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota, parent, false);
        return new MascotaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota m = mascotas.get(position);
        holder.tvNombre.setText(m.getNombre());
        holder.tvDetalle.setText(m.getEspecie() + " · " + m.getEdad()
                + (m.getEdad() == 1 ? " año" : " años"));
        holder.tvEstado.setText(m.getEstado());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(m));
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    static class MascotaViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre, tvDetalle, tvEstado;

        MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre);
            tvDetalle = itemView.findViewById(R.id.tv_detalle);
            tvEstado = itemView.findViewById(R.id.tv_estado);
        }
    }
}
