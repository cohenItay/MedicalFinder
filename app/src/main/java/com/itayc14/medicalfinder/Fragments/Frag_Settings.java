package com.itayc14.medicalfinder.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.itayc14.medicalfinder.MedicalClinicsMain;
import com.itayc14.medicalfinder.MyLocationListener;
import com.itayc14.medicalfinder.R;

/**
 * Created by itaycohen on 22.11.2016.
 */

public class Frag_Settings extends Fragment {

    private Switch sw;
    private TextView alert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_settings, container, false);
        sw = (Switch)view.findViewById(R.id.settings_switch);
        sw.setEnabled(false);
        alert = (TextView)view.findViewById(R.id.settings_text3);
        final MedicalClinicsMain mcm  = (MedicalClinicsMain)getActivity();


        if (MyLocationListener.hasLocation){
            sw.setEnabled(true);
            alert.setVisibility(View.GONE);
        }
        final SeekBar sb = (SeekBar)view.findViewById(R.id.settings_SeekBar);
        sb.setEnabled(false);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sb.setEnabled(true);
                    mcm.frag_clinicList.filterClinicsByDistance(sb.getProgress());
                }
                else {
                    sb.setEnabled(false);
                    mcm.frag_clinicList.disableLengthFilter();
                }

            }
        });

        final TextView km = (TextView)view.findViewById(R.id.settings_textKm);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                km.setText(progress+"km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MedicalClinicsMain mcm  = (MedicalClinicsMain)getActivity();
                mcm.frag_clinicList.filterClinicsByDistance(seekBar.getProgress());
            }
        });

        return view;
    }

    private void disableLengthLimitation() {

    }

    public void setSwitchEnable(boolean enable){
        alert.setVisibility(View.GONE);
        sw.setEnabled(enable);
    }

}
