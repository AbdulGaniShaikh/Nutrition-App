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


public class FragmentHeight extends Fragment {

    View view;
    NumberPicker numberPicker;
    int height = 150;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_height, container, false);

        numberPicker = view.findViewById(R.id.height_frag);

        SharedPreferences sp = getActivity().getSharedPreferences(Keys.USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(300);
        numberPicker.setValue(sp.getInt(Keys.USER_HEIGHT,150));
        editor.putInt(Keys.USER_HEIGHT,height);
        editor.commit();


        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                height = numberPicker.getValue();
                editor.putInt(Keys.USER_HEIGHT,height);
                editor.commit();
            }
        });

        return view;
    }
}