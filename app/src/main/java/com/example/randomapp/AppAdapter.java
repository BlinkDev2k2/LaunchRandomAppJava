package com.example.randomapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder>{
    private List<App> list;
    private final PackageManager packageManager;
    private final OnSwitchClickListener listener;

    public AppAdapter(Context context, OnSwitchClickListener listener) {
        this.listener = listener;
        packageManager = context.getPackageManager();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<App> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        App app = list.get(position);
        holder.imgApp.setImageDrawable(packageManager.getApplicationIcon(app.getInfo()));
        holder.nameApp.setText(packageManager.getApplicationLabel(app.getInfo()));
        holder.chooseApp.setChecked(app.isChecked());
        holder.chooseApp.setOnClickListener(view -> listener.onChangeChecked(packageManager.getApplicationLabel(app.getInfo()).toString(), !(app.isChecked())));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgApp;
        private final TextView nameApp;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch chooseApp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgApp = itemView.findViewById(R.id.imageApp);
            nameApp = itemView.findViewById(R.id.nameApp);
            chooseApp = itemView.findViewById(R.id.btnChoose);
        }
    }

    public interface OnSwitchClickListener {
        void onChangeChecked(String name, boolean state);
    }
}
