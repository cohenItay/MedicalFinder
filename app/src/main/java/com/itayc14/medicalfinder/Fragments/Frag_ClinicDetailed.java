package com.itayc14.medicalfinder.Fragments;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itayc14.medicalfinder.Clinic;
import com.itayc14.medicalfinder.DetailedClinicActivity;
import com.itayc14.medicalfinder.R;
import com.squareup.picasso.Picasso;

/**
 * Created by itaycohen on 21.11.2016.
 */

public class Frag_ClinicDetailed extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private Clinic clinic;
    private ImageView callBtn, navigateBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_clinic_detailed, container, false);

        callBtn = (ImageView)view.findViewById(R.id.frag_detailed_phoneBtn);
        callBtn.setOnClickListener(this);
        navigateBtn = (ImageView)view.findViewById(R.id.frag_detailed_navigateBtn);
        navigateBtn.setOnClickListener(this);
        TextView clinicName = (TextView)view.findViewById(R.id.frag_detailed_name);
        TextView address = (TextView)view.findViewById(R.id.frag_detailed_address);
        TextView freeText = (TextView)view.findViewById(R.id.frag_detailed_freeText);
        ImageView image = (ImageView)view.findViewById(R.id.frag_detailed_image);

        Bundle bundle = getArguments();
        if(bundle != null) {
            clinic = bundle.getParcelable("CLINIC");
            clinicName.setText(clinic.getName());
            address.setText(clinic.getAddress());
            freeText.setText(clinic.getFreeText());
            Picasso.with(getActivity()).load(clinic.getImg()).error(R.drawable.clinic).into(image);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager()
                .findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(clinic != null) {
            LatLng clinicLocation = new LatLng(clinic.getLat(), clinic.getLng());
            googleMap.addMarker(new MarkerOptions().position(clinicLocation).title(clinic.getName()));
            CameraPosition cp = new CameraPosition(clinicLocation, 13.5f, 0, 0);
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 2500, null);
        }
    }

    private void call(){
        Uri uri = Uri.parse("tel:"+clinic.getTel()); // tel is the default dialer of android!..
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        int checkPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if(checkPermission == PackageManager.PERMISSION_GRANTED)
            startActivity(intent);
        else
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, DetailedClinicActivity.CALL_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == DetailedClinicActivity.CALL_PERMISSION_CODE){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                }
            }
        }
    }
}
