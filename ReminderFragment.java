package com.example.springroll.database;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import library.CalendarAPI.WeekViewEvent;

/**
 * Created by SpringRoll on 4/30/2016.
 */
public class ReminderFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    public static final String REMINDER_EVENT_ID = "com.example.springroll.database.reminder_event_id";
    private Button hrSpin;
    private Button minSpin;
    private Button addButton;
    private AlarmSender as;
    long evID;
    WeekViewEvent event;

    public static ReminderFragment newInstance(long eventId){
        Bundle args = new Bundle();
        args.putSerializable(REMINDER_EVENT_ID, eventId);
        ReminderFragment f = new ReminderFragment();

        f.setArguments(args);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        evID = (long)getArguments().getSerializable(REMINDER_EVENT_ID);
        event = CalEventManager.get(getActivity()).getSingleEvent(evID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.reminder_fragment, container, false);

        hrSpin = (Button) v.findViewById(R.id.hrSpinner);
        hrSpin.setText("Hours:");

        List<String> hrOptions = new ArrayList<String>();

        for(int i = 0; i < 7 * 24; i++){
            hrOptions.add(""+i);
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,hrOptions);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hrSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Hours:").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hrSpin.setText(adapter.getItem(which));
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        minSpin = (Button)v.findViewById(R.id.minSpin);
        minSpin.setText("Minutes:");

        List<String> minOptions = new ArrayList<String>();

        for(int i = 0; i < 60; i++){
            minOptions.add(""+i);
        }

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, minOptions);

        minSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Minutes:").setAdapter(adapter1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        minSpin.setText(adapter1.getItem(which));
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        addButton = (Button)v.findViewById(R.id.addTheReminder);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hs = -1;
                int ms = -1;

                if(!(hrSpin.getText().toString().equals("Hours:"))) {
                    hs = Integer.parseInt(hrSpin.getText().toString());
                }

                if(!(minSpin.getText().toString().equals("Minutes:"))) {
                    ms = Integer.parseInt(minSpin.getText().toString());
                }

                if(hs != -1 && ms != -1 && (hs != 0 || ms != 0)){
                    as = new AlarmSender(getActivity(), event.getName(), hs, ms);

                    as.setalarm(hs, ms, event.getStartTime());

                    Toast.makeText(getActivity(), "Reminder Added", Toast.LENGTH_SHORT).show();

                    event.addmHr(hs);
                    event.addmMin(ms);

                   /* if(NavUtils.getParentActivityIntent(getActivity()) != null){
                        NavUtils.navigateUpFromSameTask(getActivity());
                    }*/
                }else{
                    Toast.makeText(getActivity(), "Time Not Set", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
