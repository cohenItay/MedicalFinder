package com.itayc14.medicalfinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itayc14.medicalfinder.Fragments.Frag_ClinicList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by itaycohen on 21.11.2016.
 */

public class ClinicsAdapter extends RecyclerView.Adapter<ClinicsAdapter.ViewHolder> {

    private List<Clinic> clinicList;
    private Frag_ClinicList frag_clinicList;
    private Context context;

    public ClinicsAdapter(ArrayList<Clinic> clinics, Frag_ClinicList frag_clinicList, Context context){
        clinicList = clinics;
        this.frag_clinicList = frag_clinicList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_pattern, parent, false);
        view.setOnClickListener(frag_clinicList);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Clinic clinic = clinicList.get(position);
            holder.name.setText(clinic.getName());
        if (holder.address != null)
            holder.address.setText(clinic.getAddress());
        String currentDistance = clinicList.get(position).getCurrentDistance();
        if(currentDistance != null)
            holder.distance.setText(currentDistance);
        Picasso.with(context).load(clinic.getImg()).error(R.drawable.clinic).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, address, distance;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.listView_clinic_name);
            address = (TextView)itemView.findViewById(R.id.listView_clinic_address);
            distance = (TextView)itemView.findViewById(R.id.listView_clinic_distance);
            img = (ImageView)itemView.findViewById(R.id.listView_image);
        }
    }

    public void setClinicList(ArrayList<Clinic> clinicList){
        this.clinicList = clinicList;
    }


}
