package com.miniproject.nutritionapp.ProfileFrags;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.miniproject.nutritionapp.Keys;
import com.miniproject.nutritionapp.R;


public class FragmentGender extends Fragment {

    View view;

//    RadioButton male,female;
//    RadioGroup group;
    RelativeLayout male, female;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gender, container, false);

//        group = view.findViewById(R.id.radio_frag);
//        male = view.findViewById(R.id.male_frag);
//        female = view.findViewById(R.id.female_frag);

        male = view.findViewById(R.id.profile_male);
        female = view.findViewById(R.id.profile_female);

        SharedPreferences sp = getActivity().getSharedPreferences(Keys.USER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (sp.getString(Keys.USER_GENDER,Keys.USER_MALE).equals(Keys.USER_FEMALE)){
//            group.check(female.getId());
            female.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_focus_bg));
            male.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_unfocus_bg));
        } else if (sp.getString(Keys.USER_GENDER, "none").equals(Keys.USER_MALE)) {
//            group.check(male.getId());
            male.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_focus_bg));
            female.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_unfocus_bg));
//            editor.putString(Keys.USER_GENDER,Keys.USER_MALE);
//            editor.commit();
        }

//        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if(radioGroup.getCheckedRadioButtonId()==male.getId()){
//                    editor.putString(Keys.USER_GENDER,Keys.USER_MALE);
//                    editor.commit();
//                }else if(radioGroup.getCheckedRadioButtonId()==female.getId()){
//                    editor.putString(Keys.USER_GENDER,Keys.USER_FEMALE);
//                    editor.commit();
//                }
//            }
//        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_focus_bg));
                female.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_unfocus_bg));
                editor.putString(Keys.USER_GENDER,Keys.USER_MALE);
                editor.commit();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_focus_bg));
                male.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.male_female_unfocus_bg));
                editor.putString(Keys.USER_GENDER,Keys.USER_FEMALE);
                editor.commit();
            }
        });

        return view;
    }
}