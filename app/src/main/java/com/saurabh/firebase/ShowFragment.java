package com.saurabh.firebase;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowFragment extends Fragment {

    Context context;
    ArrayList<ArrayList> arrayList;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    String androidDeviceId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context= inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_show, container, false);;
        final ListView listView = view.findViewById(R.id.ListShow);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        androidDeviceId = Settings.Secure.getString(inflater.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        arrayList = new ArrayList<>();
        Toast.makeText(context, ""+androidDeviceId, Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child(androidDeviceId.toString()).getChildren()){
                    ArrayList temp = new ArrayList();
                    temp.add(dataSnapshot1.child("Name").getValue().toString());
                    temp.add(dataSnapshot1.child("Number").getValue().toString());
                    boolean is_there = false;
                    for (int i=0 ; i<arrayList.size() ; i++){
                        if (arrayList.get(i).get(0).equals(dataSnapshot1.child("Name").getValue().toString())){
                            is_there = true;
                        }
                    }

                    if (!is_there){
                        arrayList.add(temp);
                    }
                }

                listView.setAdapter(new CustomAdapter());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setAdapter(new CustomAdapter());
        return view;
    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.each_contact , null);
            TextView name = view.findViewById(R.id.NameShow);
            TextView number = view.findViewById(R.id.NumberShow);

            name.setText(arrayList.get(i).get(0).toString());
            number.setText(arrayList.get(i).get(1).toString());

            return view;
        }
    }

    public static ShowFragment newInstance(String s) {
        ShowFragment f = new ShowFragment();

        Bundle bundle = new Bundle();
        bundle.putString("msg", s);

        f.setArguments(bundle);

        return f;
    }

}
