package com.rldev.aquagrip.ui.control;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rldev.aquagrip.MainActivity;
import com.rldev.aquagrip.R;
import com.rldev.aquagrip.databinding.FragmentControlBinding;

import java.util.ArrayList;

import me.tankery.lib.circularseekbar.CircularSeekBar;
import top.defaults.colorpicker.ColorPickerPopup;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;
    private FragmentControlBinding binding;
    private Button bt1;
    private CircularSeekBar csb,csb1;
    private TextView y,z;
    private TextInputLayout minedt,maxedt,fdtimess;
    private String tmpp,turbb,feedsched="";
    private Switch mfill,mdrain,fan,htr,lts,fv;
    private View root;
   private  ListView sched;
    private ArrayList<String> ar;
    private ArrayAdapter arrayAdapter;
    private Integer times,timess,yy,zz=1,valuetz;
    private long tStart,tEnd,tDelta;
    public Float hayt;
    public Integer hayt2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controlViewModel =
                new ViewModelProvider(this).get(ControlViewModel.class);


        root = inflater.inflate(R.layout.fragment_control, container, false);
        minedt=root.findViewById(R.id.minedt);
        maxedt=root.findViewById(R.id.maxedt);
        fdtimess=root.findViewById(R.id.fdtimes);
        bt1=root.findViewById(R.id.clrpck);
        csb=root.findViewById(R.id.circularSeekBar);
        y=root.findViewById(R.id.per);
        csb1=root.findViewById(R.id.circularSeekBar1);
        mfill=root.findViewById(R.id.manual_fills);
        lts=root.findViewById(R.id.lswitch);
        fv=root.findViewById(R.id.manual_sfills);
        sched=root.findViewById(R.id.txt6);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fvvv = database.getReference("sstat");
        DatabaseReference fant = database.getReference("fan");
        DatabaseReference heatt = database.getReference("heat");
        DatabaseReference mfilll = database.getReference("mfill");
        DatabaseReference mstat = database.getReference("mstat");
        DatabaseReference mdrainn = database.getReference("mdrain");
        DatabaseReference lightt = database.getReference("light");
        DatabaseReference fvv = database.getReference("sfill");
        DatabaseReference fig = database.getReference("feed");
        DatabaseReference clearfed = database.getReference("fedtimes");

        ar=new ArrayList<>();
     initi();
        root.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(times>1){ fig.removeValue(); fig.child("0").setValue("SCHEDULE");ar.clear();sched.setAdapter(arrayAdapter);
                clearfed.setValue("0");
                }
                else{}            }
        });
        root.findViewById(R.id.fd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(root,"Feeding Process Started",Snackbar.LENGTH_SHORT).show();
            }
        });
        root.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(root.getContext());
                builder.setTitle("CONFIRMATION");
                builder.setMessage(
                        "Minimum Temperature: "+minedt.getEditText().getText().toString()+"℃\n"+
                                "Maximum Temperature: "+maxedt.getEditText().getText().toString()+"℃\n"+
                                "Preserved Water Level: "+y.getText().toString()+"\n"+
                                "Maximum Turbidity: "+z.getText().toString()+"\n"+"Feed Times: "+fdtimess.getEditText().getText().toString()+"\n"
                );
                builder.setPositiveButton("AGREE",((dialog, which) ->{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mint = database.getReference("mintemp");
                    DatabaseReference maxt = database.getReference("maxtemp");
                    DatabaseReference pres = database.getReference("preserved");
                    DatabaseReference maxtur = database.getReference("maxtur");
                    DatabaseReference feems = database.getReference("feed");
                    DatabaseReference fdtimesss=database.getReference("feedtimes");

                    DatabaseReference updated = database.getReference("update");
                    updated.setValue("1");
                    if(fdtimess.getEditText().getText().toString().equals("0")||fdtimess.getEditText().getText().toString().isEmpty()){
                        fdtimesss.setValue("3");
                    }
                    else {
                        fdtimesss.setValue(fdtimess.getEditText().getText().toString());
                    }
                    if(minedt.getEditText().getText().toString().equals("0")||minedt.getEditText().getText().toString().isEmpty()){
                        mint.setValue("22");
                    }
                    else {
                        mint.setValue(minedt.getEditText().getText().toString());
                    }
                    if(maxedt.getEditText().getText().toString().equals("0")||maxedt.getEditText().getText().toString().isEmpty()){
                        maxt.setValue("29");
                    }
                    else {
                        maxt.setValue(maxedt.getEditText().getText().toString());
                    }
                    if(y.getText().toString().equals("0%")){
                        pres.setValue("30");
                    }
                    else {
                        String[] separated = y.getText().toString().split("%");
                        pres.setValue(separated[0]);
                    }
                    if(z.getText().toString().equals("0 NTU")){
                        maxtur.setValue("19");
                    }
                    else {
                        String[] separateds = z.getText().toString().split(" ");
                        maxtur.setValue(separateds[0]);
                    }





                } ));
                builder.setNegativeButton("Cancel",((dialog, which) ->{

                } ));
                builder.show();


            }
        });
        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                final MaterialTimePicker picker =
                        new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_12H)
                                .setHour(12)
                                .setMinute(10)
                                .setTitleText("Feeding Time Scheduler")
                                .build();
                picker.show(fm,"");
                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int j=picker.getHour();
                        int k= picker.getMinute();
                        String l="";
                        String h="";
                        String m ="";
              if(j<10){
                  h="0"+String.valueOf(j);
              }
              else{
                  h=String.valueOf(j);
              }
                if(k<10){
                            m="0"+String.valueOf(k);
                        }
                        else{
                            m=String.valueOf(k);
                        }
                        l=h+":"+m;
                   
                        DatabaseReference fid = database.getReference("feed");
                        DatabaseReference fidt = database.getReference("feedtimes");

                        fid.child(String.valueOf(times)).setValue(l);
                    ;
                    }
                });
                 }
        });
root.findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(root.getContext());
        builder.setTitle("CONFIRMATION");
        builder.setMessage(
               "Minimum Temperature: "+minedt.getEditText().getText().toString()+"℃\n"+
                "Maximum Temperature: "+maxedt.getEditText().getText().toString()+"℃\n"+
                       "Preserved Water Level: "+y.getText().toString()+"\n"+
                       "Maximum Turbidity: "+z.getText().toString()+"\n"+"Feed Times: "+fdtimess.getEditText().getText().toString()+"\n"
        );
        builder.setPositiveButton("AGREE",((dialog, which) ->{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mint = database.getReference("mintemp");
            DatabaseReference maxt = database.getReference("maxtemp");
            DatabaseReference pres = database.getReference("preserved");
            DatabaseReference maxtur = database.getReference("maxtur");
            DatabaseReference feems = database.getReference("feed");
            DatabaseReference fdt=database.getReference("feedtimes");
            DatabaseReference updated = database.getReference("update");
            updated.setValue("1");
            if(fdtimess.getEditText().getText().toString().equals("0")||fdtimess.getEditText().getText().toString().isEmpty()){
                fdt.setValue("3");
            }
            else {
                fdt.setValue(fdtimess.getEditText().getText().toString());
            }
           if(minedt.getEditText().getText().toString().equals("0")||minedt.getEditText().getText().toString().isEmpty()){
               mint.setValue("22");
           }
           else {
mint.setValue(minedt.getEditText().getText().toString());
           }
            if(maxedt.getEditText().getText().toString().equals("0")||maxedt.getEditText().getText().toString().isEmpty()){
                maxt.setValue("29");
            }
            else {
                maxt.setValue(maxedt.getEditText().getText().toString());
            }
            if(y.getText().toString().equals("0%")){
                pres.setValue("30");
            }
            else {
                String[] separated = y.getText().toString().split("%");
                pres.setValue(separated[0]);
            }
            if(z.getText().toString().equals("0 NTU")){
                maxtur.setValue("19");
            }
            else {
                String[] separateds = z.getText().toString().split(" ");
                maxtur.setValue(separateds[0]);
            }





        } ));
        builder.setNegativeButton("Cancel",((dialog, which) ->{

        } ));
        builder.show();
    }
});
        root.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference hayts = database.getReference("hayt");
                DatabaseReference hays = database.getReference("hays");
                hayts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String valuet = dataSnapshot.getValue(String.class);
                        hayt=Float.valueOf(valuet);

                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
// Failed to read value
                    }
                });
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(root.getContext());
                builder.setTitle("INITIALIZE EMPTY");
                builder.setMessage(
                        "Ensure that the aquarium is empty then click ACCEPT to use sensor read value as aquarium height."
                );
                builder.setPositiveButton("AGREE",((dialog, which) ->{
                    hays.setValue(hayt.toString());

                } ));
                builder.setNegativeButton("Cancel",((dialog, which) ->{

                } ));
                builder.show();
            }
        });
        root.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference hayts2 = database.getReference("turbid");
                DatabaseReference hays2 = database.getReference("maxtur");
                hayts2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String valuet = dataSnapshot.getValue(String.class);
                        hayt2=Integer.valueOf(valuet);

                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
// Failed to read value
                    }
                });
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(root.getContext());
                builder.setTitle("TDS Calibration:");
                builder.setMessage(
                        "Dip the TDS probe in a test substance then click ACCEPT to use sensor read value as max ppm value."
                );
                builder.setPositiveButton("AGREE",((dialog, which) ->{
                    hays2.setValue(hayt2.toString());

                } ));
                builder.setNegativeButton("Cancel",((dialog, which) ->{

                } ));
                builder.show();
            }
        });
        fv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   fvv.setValue("1");
                fvvv.setValue("1");}else{
                 fvv.setValue("0");
                    fvvv.setValue("0");}
            }
        });
        mfill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){mdrain.setChecked(false);
                   mfilll.setValue("1");
                   mstat.setValue("1");
                }else{
                    mfilll.setValue("0");
                    mstat.setValue("0");  }
            }
        });
        lts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   lightt.setValue("1"); }else{
                    lightt.setValue("0");  }
            }
        });

        mdrain=root.findViewById(R.id.manual_drains);
        mdrain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){mfill.setChecked(false);
                    mdrainn.setValue("1");
                    mstat.setValue("2");
                }else{
                    mdrainn.setValue("0");
                    mstat.setValue("0");
                }
            }
        });
        fan=root.findViewById(R.id.fswitch);

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){htr.setChecked(false);

                    fant.setValue("1");
                }
                else{


                    fant.setValue("0");
                }
            }
        });

        htr=root.findViewById(R.id.hswitch);
        htr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){fan.setChecked(false);
                     heatt.setValue("1");  }
                else{
                    heatt.setValue("0"); }
            }
        });
        z=root.findViewById(R.id.pers);

        csb.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                String message = String.format("Progress changed to %.2f, fromUser %s", progress, fromUser);
                Log.d("Main", message);
                y.setText(String.valueOf(String.format("%.0f",progress))+"%");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStopTrackingTouch");

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStartTrackingTouch");

            }
        });

        csb1.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                String message = String.format("Progress changed to %.2f, fromUser %s", progress, fromUser);
                Log.d("Main", message);
                z.setText(String.valueOf(String.format("%.0f",progress))+" PPM");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStopTrackingTouch");

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStartTrackingTouch");

            }
        });

        hwg();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
public void hwg(){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference fid = database.getReference("feed");
    DatabaseReference fidt = database.getReference("feedtimes");
    fid.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            if(dataSnapshot.exists()){
                int cnt = (int)dataSnapshot.getChildrenCount();
                fidt.setValue(String.valueOf(cnt));
                times=cnt;
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
                                sched.setDividerHeight(0);
                                sched.setAdapter(arrayAdapter);}
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
public void initi( ){
    FirebaseDatabase databases = FirebaseDatabase.getInstance();
    DatabaseReference fants = databases.getReference("fan");
    DatabaseReference heatss = databases.getReference("heat");
    DatabaseReference mfills = databases.getReference("mfill");
    DatabaseReference mdrains = databases.getReference("mdrain");
    DatabaseReference ltss = databases.getReference("light");
    DatabaseReference fvs = databases.getReference("sfill");
    DatabaseReference mintempp = databases.getReference("mintemp");
    DatabaseReference maxtmpp = databases.getReference("maxtemp");
    DatabaseReference press = databases.getReference("preserved");
    DatabaseReference maxtt = databases.getReference("maxtur");
    DatabaseReference fidtt=databases.getReference("feedtimes");
    DatabaseReference downl=databases.getReference("down");

    fants.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
           if(valuet.equals("1")) {fan.setChecked(true);}
           else{fan.setChecked(false);}

        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    heatss.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            if(valuet.equals("1")) {htr.setChecked(true);}
            else{htr.setChecked(false);}

        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    mfills.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            if(valuet.equals("1")) {mfill.setChecked(true);

            }
            else{mfill.setChecked(false);}

        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    mdrains.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            if(valuet.equals("1")) {mdrain.setChecked(true);}
            else{mdrain.setChecked(false);}

        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    fidtt.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            fdtimess.getEditText().setText(valuet);


        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    ltss.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            if(valuet.equals("1")) {lts.setChecked(true);}
            else{lts.setChecked(false);}

        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    fvs.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            if(valuet.equals("1")) {fv.setChecked(true);

            }
            else{fv.setChecked(false);}

        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    mintempp.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.
            String valuet = dataSnapshot.getValue(String.class);
            minedt.getEditText().setText(valuet);


        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });

    maxtmpp.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            maxedt.getEditText().setText(valuet);


        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    press.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            csb.setProgress(Integer.valueOf(valuet));



        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    maxtt.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
            // whenever data at this location is updated.

            String valuet = dataSnapshot.getValue(String.class);
            csb1.setProgress(Integer.valueOf(valuet));



        }


        @Override
        public void onCancelled(DatabaseError error) {
// Failed to read value

        }
    });
    }}



