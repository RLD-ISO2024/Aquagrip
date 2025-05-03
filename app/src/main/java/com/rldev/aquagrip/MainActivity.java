package com.rldev.aquagrip;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rldev.aquagrip.databinding.ActivityMainBinding;

import top.defaults.colorpicker.ColorPickerPopup;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences sharedpreferences;
    private FirebaseDatabase database;
    private long tStart,tEnd,tDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_control, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(binding.navView, navController);
        sharedpreferences = getSharedPreferences("APPS", Context.MODE_PRIVATE);

        su();
    }
public void su(){
     database = FirebaseDatabase.getInstance();
    DatabaseReference data1 = database.getReference("dailys/1");
    DatabaseReference data2 = database.getReference("dailys/2");
    DatabaseReference data3 = database.getReference("dailys/3");
    DatabaseReference data4 = database.getReference("dailys/4");
    DatabaseReference data5 = database.getReference("dailys/5");
    DatabaseReference data6 = database.getReference("dailys/6");
    DatabaseReference data7 = database.getReference("dailys/7");
    DatabaseReference changes = database.getReference("dailys/change");
    SharedPreferences.Editor editor = sharedpreferences.edit();
    // Read from the database
    data1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value1 = dataSnapshot.getValue(String.class);
            editor.putInt("1",Integer.valueOf(value1));
            editor.commit();
        }

        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    data2.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value2 = dataSnapshot.getValue(String.class);
            editor.putInt("2",Integer.valueOf(value2));
            editor.commit();
        }

        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    data3.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value3 = dataSnapshot.getValue(String.class);
            editor.putInt("3",Integer.valueOf(value3));
            editor.commit();
        }

        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    data4.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value4 = dataSnapshot.getValue(String.class);
            editor.putInt("4",Integer.valueOf(value4));
            editor. commit();
        }

        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    data5.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value5 = dataSnapshot.getValue(String.class);
            editor.putInt("5",Integer.valueOf(value5));
            editor.commit();
        }

        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    data6.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value6 = dataSnapshot.getValue(String.class);
            editor.putInt("6",Integer.valueOf(value6));
            editor.commit();
        }

        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    data7.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value7 = dataSnapshot.getValue(String.class);
            editor.putInt("7",Integer.valueOf(value7));
            editor.commit();
        }

        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });

    // Read from the database
    changes.setValue("false");

}
    public void onflash(View v){
        new ColorPickerPopup.Builder(MainActivity.this)
                .initialColor(Color.WHITE)
                .enableBrightness(true)
                .okTitle("CONFIRM")
                .cancelTitle("CANCEL")
                .showIndicator(true)
                .showValue(false)
                .build()
                .show(v, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        DatabaseReference colo = database.getReference("color");
                     //   tStart = SystemClock.elapsedRealtimeNanos();
                        colo.setValue("#" + Integer.toHexString(color).substring(2).toUpperCase()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                              //  long tEnd = SystemClock.elapsedRealtimeNanos();
                                //long tDelta = tEnd - tStart;
                                Toast.makeText(MainActivity.this, String.valueOf(tDelta), Toast.LENGTH_SHORT).show();}
                        });

                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }}
