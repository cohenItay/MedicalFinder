package com.itayc14.medicalfinder;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailedClinicActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private Clinic clinic;
    private ImageView callBtn, navigateBtn;
    public static int CALL_PERMISSION_CODE = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_detailed);

        callBtn = (ImageView)findViewById(R.id.activity_detailed_phoneBtn);
        callBtn.setOnClickListener(this);
        navigateBtn = (ImageView)findViewById(R.id.activity_detailed_navigateBtn);
        navigateBtn.setOnClickListener(this);
        TextView clinicName = (TextView)findViewById(R.id.activity_detailed_name);
        TextView address = (TextView)findViewById(R.id.activity_detailed_address);
        TextView freeText = (TextView)findViewById(R.id.activity_detailed_freeText);
        ImageView image = (ImageView)findViewById(R.id.activity_detailed_image);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        clinic = getIntent().getParcelableExtra("CLINIC");
        clinicName.setText(clinic.getName());
        address.setText(clinic.getAddress());
        freeText.setText(clinic.getFreeText());
        Picasso.with(this).load(clinic.getImg()).error(R.drawable.clinic).into(image);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng clinicLocation = new LatLng(clinic.getLat(), clinic.getLng());
        mMap.addMarker(new MarkerOptions().position(clinicLocation).title(clinic.getName()));
        CameraPosition cp = new CameraPosition(clinicLocation, 13.5f, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 2500, null);
    }

    @Override
    public void onClick(View v) {
        if (v == callBtn) {
            call();
        }else if(v== navigateBtn){
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?&daddr="+clinic.getLat()+","+clinic.getLng()));
            startActivity(intent);
        }

    }

    private void call(){
        Uri uri = Uri.parse("tel:"+clinic.getTel()); // tel is the default dialer of android!..
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if(checkPermission == PackageManager.PERMISSION_GRANTED)
            startActivity(intent);
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CALL_PERMISSION_CODE){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                }
            }
        }
    }
}
