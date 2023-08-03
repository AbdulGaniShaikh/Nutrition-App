package com.miniproject.nutritionapp.HomeActivities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miniproject.nutritionapp.R;

public class FragmentDiet extends Fragment {

    View view;
    TextView mBreakfastTitle, mMidMealTitle, mLunchTitle, mSnacksTitle, mDinnerTitle;
    String breakfast, midMeal, lunch, snacks, dinner;

    public FragmentDiet(String[] diet) {
        breakfast = diet[0];
        midMeal = diet[1];
        lunch = diet[2];
        snacks = diet[3];
        dinner = diet[4];
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diet, container, false);

        mBreakfastTitle = view.findViewById(R.id.breakfast_text1);
        mMidMealTitle = view.findViewById(R.id.midmeal_text1);
        mLunchTitle = view.findViewById(R.id.lunch_text1);
        mSnacksTitle = view.findViewById(R.id.snacks_text1);
        mDinnerTitle = view.findViewById(R.id.dinner_text1);

        mBreakfastTitle.setText(breakfast);
        mMidMealTitle.setText(midMeal);
        mLunchTitle.setText(lunch);
        mSnacksTitle.setText(snacks);
        mDinnerTitle.setText(dinner);

        return view;
    }
}