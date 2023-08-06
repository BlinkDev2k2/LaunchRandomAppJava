package com.example.randomapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<App> apps;
    private PackageManager packageManager;
    private AppAdapter adapter;
    private SharePreference sharePreference;

    @Override
    protected void onDestroy() {
        apps.clear();
        apps = null;
        packageManager = null;
        adapter = null;
        sharePreference = null;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        short i = (short) apps.size();
        List<String> nameApps = new ArrayList<>();
        for (short j = 0; j < i; ++j) {
            if (apps.get(j).isChecked()) {
                nameApps.add(apps.get(j).getInfo().packageName);
            }
        }
        sharePreference.putSwitchStatement("state", nameApps);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packageManager = getPackageManager();
        MaterialButton btnRandom = findViewById(R.id.btnRandom);
        EditText search = findViewById(R.id.ed_search);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        sharePreference = new SharePreference(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AppAdapter(this, (name, state) -> {
            for(App x : apps) {
                if(packageManager.getApplicationLabel(x.getInfo()).toString().equals(name)) {
                    x.setChecked(state);
                    break;
                }
            }
        });

        if(sharePreference.getBoolean("first")) {
            getAllApp2(sharePreference.getSwitchStatement("state"));
        } else {
            sharePreference.putBoonleanValue("first", true);
            getAllApp();
        }

        adapter.setList(apps);
        recyclerView.setAdapter(adapter);

        btnRandom.setOnClickListener(v -> openApp());

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterApp(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void openApp() {
        List<String> packageName = new ArrayList<>();
        short i = (short) apps.size();
        for(short j = 0; j < i; ++j) {
            if(apps.get(j).isChecked()) {
                packageName.add(apps.get(j).getInfo().packageName);
            }
        }
        short size = (short) packageName.size();
        if(size > 0) {
            Random random = new Random();
            short ran = (short) random.nextInt(size);
            String namePack = packageName.get(ran);
            Intent intent = packageManager.getLaunchIntentForPackage(namePack);
            if(intent != null) {
                sharePreference.putSwitchStatement("state", packageName);
                Toast.makeText(this, namePack, Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
                startActivity(intent);
            } else {
                Toast.makeText(this, "Can't open app", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void filterApp(String txt) {
        if(txt.isEmpty()) {
            adapter.setList(apps);
        } else {
            List<App> data = new ArrayList<>();
            for(App x : apps) {
                if(normalizeString(packageManager.getApplicationLabel(x.getInfo()).toString().toLowerCase().trim()).contains(normalizeString(txt.toLowerCase().trim()))) {
                    data.add(x);
                }
            }
            adapter.setList(data);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void getAllApp2(List<String> state) {
        if (state == null) {
            getAllApp();
            return;
        }
        apps = new ArrayList<>();
        List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        boolean check = false;
        for (ApplicationInfo x : applicationInfos) {
            if ((x.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                for(short i = 0; i < state.size(); ++i) {
                    if(state.get(i).equals(x.packageName)) {
                        apps.add(new App(x, true));
                        check = true;
                        state.remove(i);
                        break;
                    }
                }
                if (!check) {
                    apps.add(new App(x, false));
                }
                check = false;
            }
        }
        state.clear();
        applicationInfos.clear();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void getAllApp() {
        apps = new ArrayList<>();
        List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo x : applicationInfos) {
            if ((x.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                apps.add(new App(x, false));
            }
        }
        applicationInfos.clear();
    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}