package com.itayc14.medicalfinder.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itayc14.medicalfinder.Clinic;
import com.itayc14.medicalfinder.ClinicsAdapter;
import com.itayc14.medicalfinder.MedicalClinicsMain;
import com.itayc14.medicalfinder.MyLocationListener;
import com.itayc14.medicalfinder.OnClinicClickListener;
import com.itayc14.medicalfinder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itaycohen on 21.11.2016.
 */

public class Frag_ClinicList extends Fragment implements View.OnClickListener {
    private ArrayList<Clinic> clinics;
    private Context context;
    private ClinicsAdapter adapter;
    public RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        clinics = bundle.getParcelableArrayList("CLINICS");
        context = getActivity();

        View view = LayoutInflater.from(context).inflate(R.layout.frag_clinic_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listView);
        adapter = new ClinicsAdapter(clinics, this, context);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager rvManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(rvManager);


        return view;
    }


    public void filterClinicsByName(String query) {
        if(this.isVisible()) {
            ArrayList<Clinic> filteredList = new ArrayList<>();
            for (Clinic c : clinics) {
                String clinicName = c.getName().toLowerCase();
                if (clinicName.contains(query)) {
                    filteredList.add(c);
                }
            }
            adapter.setClinicList(filteredList);
            adapter.notifyDataSetChanged();
        }
    }

    public void filterClinicsByDistance(double distance){
        if(this.isVisible() && MyLocationListener.hasLocation) {
            ArrayList<Clinic> filteredList = new ArrayList<>();
            Location location = new Location("");
            for (Clinic c : clinics) {
                location.setLongitude(c.getLng());
                location.setLatitude(c.getLat());
                if(location.distanceTo(MyLocationListener.myLocation)/1000 < distance){
                    filteredList.add(c);
                }
            }
            adapter.setClinicList(filteredList);
            adapter.notifyDataSetChanged();
        }
    }

    public void disableLengthFilter() {
        adapter.setClinicList(clinics);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        ((OnClinicClickListener)getActivity()).changeView(position);
    }

    public void notifyAdapterOnDataChange(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
}
