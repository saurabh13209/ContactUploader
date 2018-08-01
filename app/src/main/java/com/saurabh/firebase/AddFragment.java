package com.saurabh.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class AddFragment extends Fragment {

    DatabaseReference database;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        final EditText name = view.findViewById(R.id.NameEdit);
        final EditText number  =view.findViewById(R.id.NumberEdit);
        Button next = view.findViewById(R.id.SaveEdit);

        final String androidDeviceId = Settings.Secure.getString(inflater.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        database =  FirebaseDatabase.getInstance().getReference();

        final SharedPreferences sharedPreferences = inflater.getContext().getSharedPreferences("Database",0);

        if (sharedPreferences.getString("Val","").equals("")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Val","1");
            editor.commit();
        }



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String num = sharedPreferences.getString("Val","");
                Map<String , String> map = new HashMap<>();
                map.put("Name" , name.getText().toString());
                map.put("Number",number.getText().toString());
                database.child(androidDeviceId).child("User_"+num).setValue(map);
                editor.putString("Val" , String.valueOf(Integer.valueOf(num)+1));
                editor.commit();
                Toast.makeText(inflater.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                name.setText("");
                number.setText("");
            }
        });
        return view;
    }

    public static AddFragment newInstance(String s) {
        AddFragment f = new AddFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", s);
        f.setArguments(bundle);
        return f;
    }

}
