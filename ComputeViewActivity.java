package com.example.springroll.database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by SpringRoll on 5/3/2016.
 */
public class ComputeViewActivity extends AppCompatActivity {
    private Button compute;
    private TimePicker pick;

    private int hr;
    private int min;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_layout);

        pick = (TimePicker)findViewById(R.id.picker);
        //pick.setIs24HourView(true);
        pick.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hr = hourOfDay;
                min = minute;
            }
        });

        compute = (Button)findViewById(R.id.computeButton);

        compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "hr = " + hr + ", min = " + min, Toast.LENGTH_SHORT).show();

                finish();
            }
        });

    }
}
