package com.rldev.aquagrip.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rldev.aquagrip.R;
import com.rldev.aquagrip.databinding.FragmentNotificationsBinding;


import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private ListView lv;
    private Integer cnt,cnts;
    FirebaseDatabase fb;
    private ArrayList<String> ar;
    private View root;
    private ArrayAdapter arrayAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        lv=root.findViewById(R.id.listview1);

        fb=FirebaseDatabase.getInstance();
        ar=new ArrayList<>();
        root.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference loggg=fb.getReference("logs");
           cnts=cnt;

                if(cnts>1){ loggg.removeValue(); loggg.child("0").setValue("Aquagrip Android Application");}
                else{}


            }
        });
        hehe();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void hehe(){
        DatabaseReference logg=fb.getReference("logs");
        DatabaseReference lcnt=fb.getReference("lcount");
        logg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.exists()){
                cnt = (int)dataSnapshot.getChildrenCount();
                 lcnt.setValue(String.valueOf(cnt));
                 int x=0;
                 while(x!=cnt){
                     ar.clear();
                logg.child(String.valueOf(x)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        if(dataSnapshot.exists()){
                        String valuet = dataSnapshot.getValue(String.class);

                        ar.add(valuet);
                       arrayAdapter=new ArrayAdapter(root.getContext(), R.layout.lv,ar);

                        lv.setAdapter(arrayAdapter);}
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
// Failed to read value

                    }
                });
                 x++;
                 }

            }}


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
    }
}