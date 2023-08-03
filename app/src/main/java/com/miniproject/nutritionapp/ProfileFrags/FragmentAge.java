package com.miniproject.nutritionapp.ProfileFrags;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;


public class FragmentAge extends Fragment {

    View view;
    NumberPicker numberPicker;
    int age = 18;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_age, container, false);

        numberPicker = view.findViewById(R.id.age_frag);

        SharedPreferences sp = getActivity().getSharedPreferences(Keys.USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(99);
        numberPicker.setValue(sp.getInt(Keys.USER_DOB,18));
        editor.putInt(Keys.USER_DOB,age);
        editor.commit();

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                age = numberPicker.getValue();
                editor.putInt(Keys.USER_DOB,age);
                editor.commit();
            }
        });
        return view;
    }
}