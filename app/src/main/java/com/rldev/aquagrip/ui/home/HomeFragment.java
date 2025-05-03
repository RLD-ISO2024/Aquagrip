package com.rldev.aquagrip.ui.home;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.rldev.aquagrip.MainActivity;
import com.rldev.aquagrip.R;
import com.rldev.aquagrip.SpeedometerView;
import com.rldev.aquagrip.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private CircularFillableLoaders circularFillableLoaders;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private SpeedometerView Speed;
    private TextView tmp, tmpstat, water, turbid, reserve,mintmp,maxtmp,mint,maxt,mots,valves,foodp,clrtxt;
    private ProgressBar progressBar,progressBar2,progressBar3,progressBar4;
    private ListView sched;
    private ObjectAnimator anim;
    private Switch fans,heats,fills,drains,sfills,sdrains;
    private int times,z=1,y,valuetz;
    private String feedsched="",minval,maxval;
    private long tStart,tEnd,tDelta;
    private View root;
    private ArrayList<String> ar;
    private ArrayAdapter arrayAdapter;
    private SharedPreferences sharedpreferences;
    private String maxvaluet;
    private Button strt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);
        circularFillableLoaders = root.findViewById(R.id.circularFillableLoaders);
        lv();
        ar=new ArrayList<>();
        Speed = (SpeedometerView)root.findViewById(R.id.speedometer);
        tmp=root.findViewById(R.id.tmp);
        tmpstat=root.findViewById(R.id.tmpstat);
        progressBar=root.findViewById(R.id.progress);
        progressBar2=root.findViewById(R.id.progress2);
        progressBar2.setMax(500);
        progressBar3=root.findViewById(R.id.progress3);
        water=root.findViewById(R.id.water);
        turbid=root.findViewById(R.id.turbid);
        reserve=root.findViewById(R.id.reserve);
        fans=root.findViewById(R.id.fans);
        heats=root.findViewById(R.id.heats);
        mintmp=root.findViewById(R.id.min);
        maxtmp=root.findViewById(R.id.max);
        mint=root.findViewById(R.id.mint);
        maxt=root.findViewById(R.id.maxt);
        fills=root.findViewById(R.id.fills);
        drains=root.findViewById(R.id.drains);
        mots=root.findViewById(R.id.mots);
        sfills=root.findViewById(R.id.sfills);
        sdrains=root.findViewById(R.id.sdrains);
        valves=root.findViewById(R.id.valves);
        foodp=root.findViewById(R.id.food);
        progressBar4=root.findViewById(R.id.progress4);
        sched=root.findViewById(R.id.sched);
        clrtxt=root.findViewById(R.id.lights);
        strt=root.findViewById(R.id.startbtn);
        root.findViewById(R.id.startbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference start = database.getReference("start");

                if(strt.getText().toString()=="START"){

                    start.setValue("1");

                }else{start.setValue("0");}
            }
        });


        Speed.setLabelConverter(new SpeedometerView.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

// configure value range and ticks

        return root;
         }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void lv(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("water");
        DatabaseReference tur = database.getReference("turbid");
        DatabaseReference temp = database.getReference("temp");
        DatabaseReference reserv = database.getReference("reserve");
        DatabaseReference fan = database.getReference("fan");
        DatabaseReference heat = database.getReference("heat");
        DatabaseReference mintemp = database.getReference("mintemp");
        DatabaseReference maxtemp = database.getReference("maxtemp");
        DatabaseReference mintur = database.getReference("mintur");
        DatabaseReference maxtur = database.getReference("maxtur");
        DatabaseReference mfill = database.getReference("mfill");
        DatabaseReference mdrain = database.getReference("mdrain");
        DatabaseReference mstat = database.getReference("mstat");
        DatabaseReference sfill = database.getReference("sfill");
        DatabaseReference sdrain = database.getReference("light");
        DatabaseReference sstat = database.getReference("sstat");
        DatabaseReference food = database.getReference("food");
        DatabaseReference fid = database.getReference("feed");
        DatabaseReference feedtimes = database.getReference("feedtimes");
        DatabaseReference clr = database.getReference("color");
        DatabaseReference start = database.getReference("start");


        // Read from the database
        String name;
        start.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.

                String valuet = dataSnapshot.getValue(String.class);
                if(Integer.valueOf(valuet)==1){
                    strt.setBackgroundColor(Color.parseColor("#808080"));
                    strt.setText("STOP");
                }
                else{strt.setBackgroundColor(Color.parseColor("#2196F3"));
                    strt.setText("START");
                }

            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });

        clr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.

                String valuet = dataSnapshot.getValue(String.class);
                clrtxt.setBackgroundColor(Color.parseColor(valuet));

            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        mintemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                minval=valuet;
                Float r=Float.valueOf(valuet);
                mintmp.setText("Minimum: "+r+"℃");

            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        mintur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                Float r=Float.valueOf(valuet);
                mint.setText("Minimum: "+r);

            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        maxtur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
               maxvaluet=valuet;
                Float r=Float.valueOf(valuet);
                maxt.setText("Maximum: "+r);
                tur.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String valuet = dataSnapshot.getValue(String.class);

                        if(Float.valueOf(valuet)<=Float.valueOf(maxvaluet)){circularFillableLoaders.setColor(Color.parseColor("#B22196F3"));}
                        else if(Float.valueOf(valuet)>Float.valueOf(maxvaluet)&&Float.valueOf(valuet)<50){circularFillableLoaders.setColor(Color.parseColor("#79E1DDDD"));}
                        else  if(Float.valueOf(valuet)>=50){circularFillableLoaders.setColor(Color.parseColor("#B2A9844F"));}
                        Float r=Float.valueOf(valuet);
                        int s = (int) Math.round(r);
                        progressBar2.setProgress(s);
                        turbid.setText(String.valueOf(s)+"   ");
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
// Failed to read value

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });

        maxtemp.addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                                              // whenever data at this location is updated.
                                              String valuet = dataSnapshot.getValue(String.class);
                                              maxval=valuet;
                                              Float r = Float.valueOf(valuet);
                                              maxtmp.setText("Maximum: " + r + "℃");

                                          }


                                          @Override
                                          public void onCancelled(DatabaseError error) {
// Failed to read value

                                          }
                                      });
        fan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    fans.setChecked(false);
                }
                else if(valuet.equals("1")){
                    fans.setChecked(true);

                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });

        fid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.exists()){
                    int cnt = (int)dataSnapshot.getChildrenCount();

                    int x=1;
                    while(x!=cnt){
                        ar.clear();
                        fid.child(String.valueOf(x)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                if(dataSnapshot.exists()){
                                    String valuet = dataSnapshot.getValue(String.class);

                                    ar.add(valuet);
                                    arrayAdapter=new ArrayAdapter(root.getContext(), R.layout.lvs,ar);
                                    sched.setAdapter(arrayAdapter);
                                    sched.setDividerHeight(0);

                                }
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
        mfill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    fills.setChecked(false);
                }
                else if(valuet.equals("1")){
                    fills.setChecked(true);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        mstat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    mots.setText("Inactive");
                    mots.setTextColor(Color.parseColor("#388e3c"));
                }
                else if(valuet.equals("1")){
                    mots.setText("Filling");
                    mots.setTextColor(Color.BLUE);
                }
                else if(valuet.equals("2")){
                    mots.setText("Draining");
                    mots.setTextColor(Color.RED);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        sdrain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    sdrains.setChecked(false);
                }
                else if(valuet.equals("1")){
                    sdrains.setChecked(true);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        sfill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    sfills.setChecked(false);
                }
                else if(valuet.equals("1")){
                    sfills.setChecked(true);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        sstat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    valves.setText("Inactive");
                    valves.setTextColor(Color.parseColor("#388e3c"));
                }
                else if(valuet.equals("1")){
                    valves.setText("Filling");
                    valves.setTextColor(Color.BLUE);
                }
                else if(valuet.equals("2")){
                    valves.setText("Draining");
                    valves.setTextColor(Color.RED);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        mdrain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    drains.setChecked(false);
                }
                else if(valuet.equals("1")){
                    drains.setChecked(true);
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        heat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String valuet = dataSnapshot.getValue(String.class);
                if(valuet.equals("0")){
                    heats.setChecked(false);
                }
                else if(valuet.equals("1")){
                    heats.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value.equals("nan")) {
                    tmp.setText("Temperature: "+ "nan");
                } else {
                    Float r = Float.valueOf(value);
                    tmp.setText("Temperature: " + r + "℃");
                    if (r <= Integer.valueOf(minval)) {
                        tmpstat.setText("Low");
                        tmpstat.setTextColor(Color.BLUE);
                    } else if (r > Integer.valueOf(minval) && r <= Integer.valueOf(maxval)) {
                        tmpstat.setText("Normal");
                        tmpstat.setTextColor(Color.parseColor("#388e3c"));
                    } else if (r > Integer.valueOf(maxval)) {
                        tmpstat.setText("High");
                        tmpstat.setTextColor(Color.RED);
                    }
                    Speed.setMaxSpeed(50);
                    Speed.setMajorTickStep(10);
                    Speed.setMinorTicks(0);

// Configure value range colors
                    Speed.addColoredRange(0, Integer.valueOf(minval), Color.BLUE);
                    Speed.addColoredRange(Integer.valueOf(minval), Integer.valueOf(maxval), Color.GREEN);
                    Speed.addColoredRange(Integer.valueOf(maxval), 50, Color.RED);
                    Speed.setSpeed(r, 500, 500);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        food.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Float r=Float.valueOf(value);
                int s = (int) Math.round(r);
                progressBar4.setProgress(s);
                foodp.setText(String.valueOf(s)+"%");


            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        reserv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Float r=Float.valueOf(value);
                int s = (int) Math.round(r);
                progressBar3.setProgress(s);
                reserve.setText(String.valueOf(s)+"%");


            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Float r=Float.valueOf(value);
                int s = (int) Math.round(r);
                int a=90*s;
                circularFillableLoaders.setProgress(100-(a/100));
                progressBar.setProgress(s);
                water.setText(String.valueOf(s)+"%");
                   }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
    }

}