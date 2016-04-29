package com.example.springroll.database;

/**
 * Created by SpringRoll on 4/23/2016.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Scroller;
import android.widget.Switch;

import java.util.Calendar;

import library.CalendarAPI.DateTimePickerDialog;
import library.CalendarAPI.WeekViewEvent;

/**
 * Created by SpringRoll on 3/30/2016.
 */
public class EventFragment extends Fragment implements DateTimePickerDialog.DateTimeListener{

    public static final String EXTRA_EVENT_ID = "com.example.springroll.database.event_id";
    public WeekViewEvent getmEvent() {
        return mEvent;
    }

    public void setmEvent(WeekViewEvent mEvent) {
        this.mEvent = mEvent;
    }

    private boolean mSave = false, mEdit = false, mDelete = false, mCancel = false, mAllDay = false;
    private WeekViewEvent mEvent;
    private Button mStartDateButton, mEndDateButton, deleteButton;
    private EditText mTitle, mLocation, mNotes;
    private Switch mAllDaySwitch;
    private Calendar calendar, mFromDate, mToDate;
    private int year, month, day, hour,minute,am_pm = -2;
    //private RatingBar mPriority;
    private RadioGroup mPriority;
    //private RadioButton zero, one, two, three, four, five;
    private CheckBox m,t,w,r,f,a,s;
    long eventId;

    private boolean edit = false;
    private boolean viewing = true;

    public static EventFragment newInstance(long eventId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_EVENT_ID, eventId);

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

       // mEvent = new WeekViewEvent();
         eventId = (long)getArguments().getSerializable(EXTRA_EVENT_ID);

       // if(eventId == 0){
         //   mEvent = new WeekViewEvent(34535, "new event", Calendar.getInstance(), Calendar.getInstance());
        //}else {
            mEvent = CalEventManager.get(getActivity()).getSingleEvent(eventId); //Need to fix this one
        //}
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_event,container,false);
        //setHasOptionsMenu(true);


            if (NavUtils.getParentActivityName(getActivity()) != null) {
               // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }

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

        mFromDate = mEvent.getStartTime();//Calendar.getInstance();
        //this is the test
       // mFromDate.set(Calendar.HOUR_OF_DAY, hour);
        //mFromDate.set(Calendar.MINUTE, minute);
       // mFromDate.set(Calendar.MONTH, month);
      //  mFromDate.set(Calendar.YEAR, year);
        //mFromDate.set(Calendar.DATE, day);
      //  mEvent.setStartTime(mFromDate);
        //end debug
        mStartDateButton.setText(setString(am_pm, mStartDateButton, mAllDay));


        mStartDateButton.setText(setString(am_pm, mStartDateButton, mAllDay));
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(mStartDateButton, am_pm);
                //mFromDate = Calendar.getInstance();
                //mFromDate.set(year, month, day, hour, minute);
                mFromDate.set(Calendar.HOUR_OF_DAY, hour);
                mFromDate.set(Calendar.MINUTE, minute);
                mFromDate.set(Calendar.MONTH, month);
                mFromDate.set(Calendar.YEAR, year);
                mFromDate.set(Calendar.DATE, day);
                //mEvent.setStartTime(mFromDate);
                if (mFromDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                    mSave = false;
                    mStartDateButton.setPaintFlags(mStartDateButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    mSave = true;
                    mStartDateButton.setPaintFlags(0);
                }
            }
        });

        mEndDateButton = (Button)v.findViewById(R.id.end_date_button);

        //This is the test
        mToDate = mEvent.getEndTime();//Calendar.getInstance();
       // mToDate.set(Calendar.HOUR_OF_DAY, hour+1);
       // mToDate.set(Calendar.MINUTE,minute);
       // mToDate.set(Calendar.MONTH, month);
       // mToDate.set(Calendar.YEAR, year);
       // mToDate.set(Calendar.DATE, day);
       // mEvent.setEndTime(mToDate);
        //end debug
        mEndDateButton.setText(setString(am_pm, mEndDateButton, mAllDay));


        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(mEndDateButton, am_pm);
                //mToDate = Calendar.getInstance();
                mToDate.set(year, month, day, hour, minute);
                //mToDate.set(Calendar.HOUR_OF_DAY,hour);
               // mToDate.set(Calendar.MINUTE,minute);
               /// mToDate.set(Calendar.MONTH,month);
               // mToDate.set(Calendar.YEAR, year);
                //mToDate.set(Calendar.DATE,day);
              //  mEvent.setEndTime(mToDate);
                if(mToDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()){
                    mSave = false;
                    mEndDateButton.setPaintFlags(mEndDateButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    mSave = true;
                    mEndDateButton.setPaintFlags(0);
                }
            }
        });

        deleteButton = (Button)v.findViewById(R.id.delButton);
        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        mAllDaySwitch = (Switch)v.findViewById(R.id.switch1);
        mAllDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAllDay = true;
                    mEvent.setAllDay(true);
                    mStartDateButton.setText(setString(am_pm, mStartDateButton, mAllDay));
                    mEndDateButton.setText(setString(am_pm, mEndDateButton, mAllDay));
                } else {
                    mAllDay = false;
                    mEvent.setAllDay(false);
                    mStartDateButton.setText(setString(am_pm, mStartDateButton, mAllDay));
                    mEndDateButton.setText(setString(am_pm, mEndDateButton, mAllDay));
                }
            }
        });

        mNotes = (EditText)v.findViewById(R.id.notescroll);
        mNotes.setText(mEvent.getmNotes());
        mNotes.setEnabled(true);
        mNotes.setTextColor(Color.BLACK);
        mNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEvent.setmNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        m = (CheckBox)v.findViewById(R.id.mon);
        if(mEvent.getmRepeat().indexOf("M") >= 0 )
            m.setChecked(true);

        t = (CheckBox)v.findViewById(R.id.tue);
        if(mEvent.getmRepeat().indexOf("T") >= 0 )
            t.setChecked(true);

        w = (CheckBox)v.findViewById(R.id.wed);
        if(mEvent.getmRepeat().indexOf("W") >= 0 )
            w.setChecked(true);

        r = (CheckBox)v.findViewById(R.id.thu);
        if(mEvent.getmRepeat().indexOf("R") >= 0 )
            r.setChecked(true);

        f = (CheckBox)v.findViewById(R.id.fri);
        if(mEvent.getmRepeat().indexOf("F") >= 0 )
            f.setChecked(true);

        a = (CheckBox)v.findViewById(R.id.sat);
        if(mEvent.getmRepeat().indexOf("A") >= 0 )
            a.setChecked(true);

        s = (CheckBox)v.findViewById(R.id.sun);
        if(mEvent.getmRepeat().indexOf("S") >= 0 )
            s.setChecked(true);


        //String rad = "radio"+mEvent.getmPriority();
        mPriority = (RadioGroup)v.findViewById(R.id.radioGroop);


        if(mEvent.getmPriority() == 0)
             mPriority.check(R.id.radio0);

        if(mEvent.getmPriority() == 1)
            mPriority.check(R.id.radio1);

        if(mEvent.getmPriority() == 2)
            mPriority.check(R.id.radio2);

        if(mEvent.getmPriority() == 3)
            mPriority.check(R.id.radio3);

        if(mEvent.getmPriority() == 4)
            mPriority.check(R.id.radio4);

        if(mEvent.getmPriority() == 5)
            mPriority.check(R.id.radio5);


        mPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio0){
                    mEvent.setmPriority(0);
                }else if(checkedId == R.id.radio1){
                    mEvent.setmPriority(1);
                }else if(checkedId == R.id.radio2){
                    mEvent.setmPriority(2);
                }else if(checkedId == R.id.radio3){
                    mEvent.setmPriority(3);
                }else if(checkedId == R.id.radio4){
                    mEvent.setmPriority(4);
                }else{
                    mEvent.setmPriority(5);
                }
            }
        });

       setStuff(false);

        return v;
    }

    public StringBuilder setString(int am_pm,Button b,Boolean mAllDay){
        StringBuilder text;
        if(mAllDay){
            text = new StringBuilder().append(month).append("/").append(day).append("/").append(year);
        }
        else if(am_pm != -1 && b.getId() == R.id.start_date_button){
            text = new StringBuilder().append(month).append("/").append(day).append("/").append(year).append("  -  ").append(hour).append(":").append(minute).append(" ").append(am_pm == Calendar.AM ? "AM" : "PM");
        }else{
            text = new StringBuilder().append(month).append("/").append(day).append("/").append(year).append("  -  ").append(hour+1).append(":").append(minute).append(" ").append(am_pm == Calendar.AM ? "AM" : "PM");
        }
        return text;
    }

    private void showDateTimeDialog(Button b, int am_pm) {
        DateTimePickerDialog pickerDialog = new DateTimePickerDialog(getActivity(), false, this);
        pickerDialog.show();
        b.setText(setString(am_pm, b, mAllDay));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(mSave) {
            menu.findItem(R.id.delete_event).setVisible(false);
            menu.findItem(R.id.edit_event).setVisible(false);
            menu.findItem(R.id.cancel_event).setVisible(true);
            menu.findItem(R.id.save_event).setVisible(true);
        }
        else {
            //menu.findItem(android.R.id.home).setVisible(false);
            menu.findItem(R.id.delete_event).setVisible(false);
            menu.findItem(R.id.edit_event).setVisible(true);
            menu.findItem(R.id.cancel_event).setVisible(false);
            menu.findItem(R.id.save_event).setVisible(false);
        }
    }

    public void setStuff(boolean val){
        mStartDateButton.setEnabled(val);
        mEndDateButton.setEnabled(val);
        mTitle.setEnabled(val);
        mLocation.setEnabled(val);
        mAllDaySwitch.setEnabled(val);
        mNotes.setEnabled(val);
        m.setEnabled(val);
        t.setEnabled(val);
        w.setEnabled(val);
        r.setEnabled(val);
        f.setEnabled(val);
        a.setEnabled(val);
        s.setEnabled(val);

        for(int i = 0; i < mPriority.getChildCount(); i++){
            ((RadioButton)mPriority.getChildAt(i)).setEnabled(val);
        }
    }

    public String rep(){
        String newRepeat = "";
        if(m.isChecked()){
            newRepeat += "M";
        }

        if(t.isChecked()){
            newRepeat += "T";
        }

        if(w.isChecked()){
            newRepeat += "W";
        }

        if(r.isChecked()){
            newRepeat += "R";
        }

        if(f.isChecked()){
            newRepeat += "F";
        }

        if(a.isChecked()){
            newRepeat += "A";
        }

        if(s.isChecked()){
            newRepeat += "S";
        }

        return newRepeat;
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
                mEvent.setmRepeat(rep());
                if(NavUtils.getParentActivityIntent(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

                return true;

            case R.id.cancel_event:
                mSave = false;
                setStuff(false);
                getActivity().invalidateOptionsMenu();

                /*if(NavUtils.getParentActivityIntent(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }*/
                return true;

            case R.id.delete_event:
                //mSave = false;
                return true;
            case R.id.edit_event:
                mSave = true;
                getActivity().invalidateOptionsMenu();
                //This is the test need to have method for this!
               setStuff(true);
                //end test
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause() {
        super.onPause();
        //Save the instance
        //CalEventManager.get(getActivity()).saveEvents();
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK,null);
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

    public static class DateTimePickerFragment extends DialogFragment implements DateTimePickerDialog.DateTimeListener {
        private int year,month,day,am_pm;
        private int hour,minute;
        DateTimePickerDialog.DateTimeListener onDateSet;

        public DateTimePickerFragment(){}

        public void setCallBack(DateTimePickerDialog.DateTimeListener ondate){
            onDateSet = ondate;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DateTimePickerDialog(getActivity(),false,this);
        }

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
            hour = args.getInt("hour");
            minute = args.getInt("minute");
        }

        @Override
        public void onDateTimeSelected(int year, int month, int day, int hour, int min, int am_pm) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = min;
            this.am_pm = am_pm;
        }
    }

}



