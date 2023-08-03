package com.miniproject.nutritionapp.ProfileFrags;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;


public class FragmentWeight extends Fragment {

    NumberPicker kg;
    View view;
    int weight = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weight, container, false);

        kg = view.findViewById(R.id.kg_frag);
//        gm = view.findViewById(R.id.gm_frag);

        SharedPreferences sp = getActivity().getSharedPreferences(Keys.USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

//        String weight = sp.getInt(Keys.USER_WEIGHT,"50");
//        int kjInt = Integer.parseInt(weight);
//        int gmInt = Integer.parseInt(weight.substring(weight.length()-1));

        kg.setMinValue(10);
        kg.setMaxValue(200);
        kg.setValue(sp.getInt(Keys.USER_WEIGHT,50));
        editor.putInt(Keys.USER_WEIGHT, weight);
        editor.commit();
//        gm.setMinValue(0);
//        gm.setMaxValue(9);
//        gm.setValue(gmInt);


        kg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                weight = kg.getValue();
                editor.putInt(Keys.USER_WEIGHT,weight);
                editor.commit();
            }
        });
//        gm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
//                editor.putString(Keys.USER_WEIGHT,kg.getValue()+"."+gm.getValue());
//                editor.commit();
//            }
//        });

        return view;
    }
}