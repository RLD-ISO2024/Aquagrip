package com.rldev.aquagrip.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rldev.aquagrip.R;
import com.rldev.aquagrip.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private BarChart barChart;
    private ArrayList<BarEntry> temp;
    private String stringOneTo10,stringElevenTo20,stringTwenty1To30,stringThirty1To40,string41T050,string51T060,string61;
    private SharedPreferences sharedpreferences;
    private String current,prev;
    private Integer pointsplotted=1,graphinterval=0;
    private Viewport vp;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {


    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        barChart=root.findViewById(R.id.bar);

        ArrayList <BarEntry> temp = new ArrayList<>();
        sharedpreferences = this.getActivity().getSharedPreferences("APPS", Context.MODE_PRIVATE);
        int o1=sharedpreferences.getInt("1",0);
        int t2=sharedpreferences.getInt("2",0);
        int t3=sharedpreferences.getInt("3",0);
        int f4=sharedpreferences.getInt("4",0);
        int f5=sharedpreferences.getInt("5",0);
        int s6=sharedpreferences.getInt("6",0);
        int s7=sharedpreferences.getInt("7",0);
        temp.add(new BarEntry(0,o1));
        temp.add(new BarEntry(1,t2));
        temp.add(new BarEntry(2,t3));
        temp.add(new BarEntry(3,f4));
        temp.add(new BarEntry(4,f5));
        temp.add(new BarEntry(5,s6));
        temp.add(new BarEntry(6,s7));
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Sun","Mon","Tue","Wed","Thu","Fri","Sat"));
        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));


        BarDataSet barDataSet=new BarDataSet(temp,"Temperature");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.setTouchEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.getDescription().setText("");
        barChart.animateY(2000);
        fs();
        GraphView graph = (GraphView)root.findViewById(R.id.graph);

      vp=graph.getViewport();
        vp.setScrollable(true);

        vp.setXAxisBoundsManual(true);
        graph.addSeries(series);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void changed(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("dailys/change");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value1 = dataSnapshot.getValue(String.class);
                if(value1.equals("true")){
                    updateChart();
                    myRef.setValue("false");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });

    }

    public void fs(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference data1 = database.getReference("dailys/1");
        DatabaseReference data2 = database.getReference("dailys/2");
        DatabaseReference data3 = database.getReference("dailys/3");
        DatabaseReference data4 = database.getReference("dailys/4");
        DatabaseReference data5 = database.getReference("dailys/5");
        DatabaseReference data6 = database.getReference("dailys/6");
        DatabaseReference data7 = database.getReference("dailys/7");
        DatabaseReference changes = database.getReference("dailys/change");
        DatabaseReference temp = database.getReference("temp");
        DatabaseReference real = database.getReference("real");
        // Read from the database

        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value30 = dataSnapshot.getValue(String.class);
                changes.setValue("true");
                current=value30;
                prev=current;
                int z=3;


                    series.appendData(new DataPoint(pointsplotted, Float.valueOf(value30)), true, pointsplotted);
                pointsplotted++;
                    vp.setMaxX(pointsplotted);
                    vp.setMinX(0);
                    vp.setMinY(0);



            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });
        data1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value1 = dataSnapshot.getValue(String.class);
                stringOneTo10=value1;
                changes.setValue("true");
                changed();
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
                stringElevenTo20=value2;
                changes.setValue("true");
                changed();
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
                stringTwenty1To30=value3;
                changes.setValue("true");
                changed();
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
                stringThirty1To40=value4;
                changes.setValue("true");
                changed();
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
                string41T050=value5;
                changes.setValue("true");
                changed();
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
                string51T060=value6;
                changes.setValue("true");
                changed();
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
                string61=value7;
                changes.setValue("true");
                changed();
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value

            }
        });

    }
    private class MyFormatter implements IValueFormatter{
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value+"$";
        }
    }
    private void updateChart(){

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(0,Integer.valueOf(stringOneTo10)));
            barEntries.add(new BarEntry(1,Integer.valueOf(stringElevenTo20)));
            barEntries.add(new BarEntry(2, Integer.valueOf(stringTwenty1To30)));
            barEntries.add(new BarEntry(3, Integer.valueOf(stringThirty1To40)));
            barEntries.add(new BarEntry(4, Integer.valueOf(string41T050)));
            barEntries.add(new BarEntry(5, Integer.valueOf(string51T060)));
            barEntries.add(new BarEntry(6, Integer.valueOf(string61)));

            BarDataSet set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(barEntries);
            barChart.setFitBars(true);
            barChart.setTouchEnabled(false);
            barChart.setScaleEnabled(false);
            barChart.setPinchZoom(false);
            barChart.getDescription().setText("");
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

            barChart.invalidate();
        }

    }
}