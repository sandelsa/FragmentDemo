package com.example.springroll.database;

import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.util.Calendar;
import java.util.UUID;

import library.CalendarAPI.DateTimePickerDialog;
import library.CalendarAPI.WeekViewEvent;

/**
 * Created by SpringRoll on 3/30/2016.
 */
public class EventActivity extends Fragment implements DateTimePickerDialog.DateTimeListener{
    public WeekViewEvent getmEvent() {
        return mEvent;
    }

    public void setmEvent(WeekViewEvent mEvent) {
        this.mEvent = mEvent;
    }

    private static final String ARG_ID = "event_id";

    private boolean mSave = false;
    private WeekViewEvent mEvent;
    private Button mStartDateButton, mEndDateButton;
    private EditText mTitle, mLocation;
    private Switch mAllDaySwitch;
    private Calendar calendar, mFromDate, mToDate;
    //more fields
    private int year, month, day, hour,minute, eyear, emonth, eday, ehour,eminute, am_pm = -2;


    //new instance of fragemnt which passes in an event
    public static EventActivity newInstance(WeekViewEvent w) {
        Bundle args = new Bundle();
       // args.putSerializable(ARG_ID, eventId);

        EventActivity fragment = new EventActivity();
        fragment.setmEvent(w);
    //    fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//for menu

       // mEvent = new WeekViewEvent();

        mFromDate = calendar = mEvent.getStartTime();//Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month =calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);


//set end date
        mToDate = mEvent.getEndTime();
        eyear = mToDate.get(Calendar.YEAR);
        emonth = mToDate.get(Calendar.MONTH)+1;
        eday = mToDate.get(Calendar.DAY_OF_MONTH);
        ehour = mToDate.get(Calendar.HOUR);
        eminute = mToDate.get(Calendar.MINUTE);
        //showDate(year, month+1, day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_event,container,false);

        mTitle = (EditText)v.findViewById(R.id.event_title);
        mTitle.setText(mEvent.getName());

        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEvent.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //This space intentionally left blank
            }
        });

        mLocation = (EditText)v.findViewById(R.id.event_location);
        mLocation.setText(mEvent.getLocation());
        mLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEvent.setLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //This space intentionally left blank
            }
        });

        mStartDateButton = (Button)v.findViewById(R.id.start_date_button);
        mStartDateButton.setText(setString(am_pm, mStartDateButton));
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFromDate.set(year, month, day, hour, minute);
                mEvent.setStartTime(mFromDate);
                showDateTimeDialog(mStartDateButton, am_pm);

                //if(mFromDate.getTimeInMillis() > mEvent.getEndTime().getTimeInMillis()){
                //    mSave = false;
                //    mStartDateButton.setPaintFlags(mStartDateButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //}else{
                //    mSave = true;
                //    mStartDateButton.setPaintFlags(0);
                   mStartDateButton.setText(setString(am_pm,mStartDateButton));
                //}
            }
        });

        mEndDateButton = (Button)v.findViewById(R.id.end_date_button);
        mEndDateButton.setText(setString(am_pm,mEndDateButton));
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToDate.set(year, month, day, hour, minute);
                mEvent.setEndTime(mToDate);
                showDateTimeDialog(mEndDateButton, am_pm);

                //if(mToDate.getTimeInMillis() < mEvent.getStartTime().getTimeInMillis()){
                 //    mSave = false;
                 //   mEndDateButton.setPaintFlags(mEndDateButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //}else{
                 //   mSave = true;
                 //   mEndDateButton.setPaintFlags(0);
                   mEndDateButton.setText(setString(am_pm,mEndDateButton));
                //}

            }
        });

        mAllDaySwitch = (Switch)v.findViewById(R.id.switch1);
        mAllDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    am_pm = 2;
                    mEvent.setAllDay(true);
                    mStartDateButton.setText(setString(am_pm,mStartDateButton));
                    mEndDateButton.setText(setString(am_pm,mEndDateButton));
                }else{
                    am_pm = -2;
                    mEvent.setAllDay(false);
                    mStartDateButton.setText(setString(am_pm,mStartDateButton));
                    mEndDateButton.setText(setString(am_pm,mEndDateButton));
                }
            }
        });
        return v;
    }

    public StringBuilder setString(int am_pm,Button b){
        StringBuilder text;
        if(am_pm == 2){
            text = new StringBuilder().append(month).append("/").append(day).append("/").append(year);
        }
        else if(am_pm != -1 && b.getId() == R.id.start_date_button){
            text = new StringBuilder().append(month).append("/").append(day).append("/").append(year).append("  -  ").append(hour).append(":").append(minute).append(" ").append(am_pm == Calendar.AM ? "AM" : "PM");
        }else{ //update with end date data
            text = new StringBuilder().append(emonth).append("/").append(eday).append("/").append(eyear).append("  -  ").append(ehour).append(":").append(eminute).append(" ").append(am_pm == Calendar.AM ? "AM" : "PM");
        }
        return text;
    }

    private void showDateTimeDialog(Button b, int am_pm) {
        DateTimePickerDialog pickerDialog = new DateTimePickerDialog(getActivity(), false, this);
        pickerDialog.show();
        b.setText(setString(am_pm,b));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(mSave)
            menu.findItem(R.id.delete_event).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityIntent(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;

            case R.id.save_event:
                mSave = true;
                return true;

            case R.id.cancel_event:
                return true;

            case R.id.delete_event:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateTimeSelected(int year, int month, int day, int hour, int min, int am_pm) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = min;
        this.am_pm = am_pm;
        //String text = day + "/" + month + "/" + year + " - " + hour + ":" + min;
       // if (am_pm != -1)
            //text = text + (am_pm == Calendar.AM ? "AM" : "PM");
    }

}


