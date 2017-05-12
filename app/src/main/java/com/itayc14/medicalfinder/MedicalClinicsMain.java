package com.itayc14.medicalfinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.itayc14.medicalfinder.Fragments.Frag_ClinicDetailed;
import com.itayc14.medicalfinder.Fragments.Frag_ClinicList;
import com.itayc14.medicalfinder.Fragments.Frag_Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MedicalClinicsMain extends AppCompatActivity implements OnClinicClickListener {

    private ArrayList<Clinic> clinics;
    public Frag_ClinicList frag_clinicList;
    private Frag_Settings frag_settings;
    private LocationManager lcManager;
    private MyLocationListener locationListener;
    private final static int LOCATION_PERMISSION_CODE = 24;
    private DistanceViewUpdater dvu;
    private boolean isLandscape;
    //hi there

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_clinics);

        initiateFragments();
        initiateToolBar();

        lcManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        //pop up gps setting if not turned on
        if (!lcManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Toast.makeText(this, "please enable GPS location", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        locationListener = new MyLocationListener(this, dvu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocation();
    }


    private void initiateToolBar() {
        Toolbar toolBar = (Toolbar)findViewById(R.id.toolbar);
        toolBar.inflateMenu(R.menu.options_menu);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.settings) {
                    if(frag_settings.isVisible()) {
                        getFragmentManager().popBackStack();
                    }else {
                        getFragmentManager()
                                .beginTransaction()
                                .addToBackStack(null)
                                .add(R.id.main_fragment, frag_settings)
                                .commit();
                    }
                }
                return false;
            }
        });
        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                frag_clinicList.filterClinicsByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                frag_clinicList.filterClinicsByName(newText);
                return false;
            }
        });
    }

    private void initiateFragments() {
        frag_clinicList = new Frag_ClinicList();
        frag_settings = new Frag_Settings();
        Bundle bundle = new Bundle();
        clinics = getClinicsList();
        bundle.putParcelableArrayList("CLINICS", clinics);
        frag_clinicList.setArguments(bundle);
        Frag_ClinicDetailed frag_clinicDetailed = new Frag_ClinicDetailed();

        if(findViewById(R.id.landscape_detailed_container) != null){
            isLandscape = true;
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("CLINIC", clinics.get(0));
            frag_clinicDetailed.setArguments(bundle2);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.landscape_detailed_container, frag_clinicDetailed)
                    .commit();
        }else {
            isLandscape = false;
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, frag_clinicList).commit();
        }

        dvu = new DistanceViewUpdater() {
            @Override
            public void updateDistance() {
                findViewById(R.id.main_progressBarLayout).setVisibility(View.GONE);
                for(int i=0; i<clinics.size(); i++) {
                    Clinic c = clinics.get(i);
                    c.setCurrentDistance(locationListener.distanceToAsString(c.getLat(), c.getLng()));
                    //View listRow = frag_clinicList.recyclerView.getLayoutManager().findViewByPosition(i);
                    frag_clinicList.notifyAdapterOnDataChange();
                    /*if (listRow != null) { //might be filtered
                        TextView distance = (TextView) listRow.findViewById(R.id.listView_clinic_distance);
                        distance.setText(c.getCurrentDistance());
                    }*/
                }
            }

            @Override
            public void enableDistanceLimitationSetting(boolean enable) {
                if(frag_settings.isVisible())
                    frag_settings.setSwitchEnable(true);
            }
        };


    }

    private ArrayList<Clinic> getClinicsList() {
        ArrayList<Clinic> clinics = new ArrayList<>();
        try {

            JSONArray clinicsJSON = new JSONObject(readClinicsJsonFile()).getJSONArray("Clinics");
            for (int i=0; i<clinicsJSON.length(); i++){
                clinics.add(new Clinic(clinicsJSON.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return clinics;
    }

    private String readClinicsJsonFile() {
        InputStream is = getResources().openRawResource(R.raw.clinics);
        StringBuilder sBuilder = new StringBuilder();
        char[] buffer = new char[1024];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) > -1) {
                sBuilder.append(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sBuilder.toString();
    }

    public void requestLocation(){
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkPermission == PackageManager.PERMISSION_GRANTED)
            lcManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 50, locationListener);
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkPermission == PackageManager.PERMISSION_GRANTED)
            lcManager.removeUpdates(locationListener);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void changeView(int position) {
        // Toast.makeText(this, "changeView", Toast.LENGTH_SHORT).show();
        if(!isLandscape){
            Intent intent = new Intent(this, DetailedClinicActivity.class);
            intent.putExtra("CLINIC", clinics.get(position));
            startActivity(intent);
        }
        else{
            Bundle bundle = new Bundle();
            bundle.putParcelable("CLINIC", clinics.get(position));
            Frag_ClinicDetailed frag_clinicDetailed = new Frag_ClinicDetailed();
            frag_clinicDetailed.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.landscape_detailed_container, frag_clinicDetailed)
                    .commit();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_CODE){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestLocation();
                }
            }
        }
    }
}
