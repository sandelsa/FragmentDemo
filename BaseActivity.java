package com.example.springroll.database;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.springroll.database.SignIn.SignInActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import library.CalendarAPI.DateTimeInterpreter;
import library.CalendarAPI.MonthLoader;
import library.CalendarAPI.WeekView;
import library.CalendarAPI.WeekViewEvent;
import library.UserFunctions;

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public abstract class BaseActivity extends SingleFragmentActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, DialogInterface.OnClickListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private UserFunctions mFunction;
    //private Button button;
    private FragmentManager fm = getFragmentManager();
    //protected abstract Fragment createdFragment();

    protected long clickid;
   // private static final String EXTRA_ID = "com.example.springroll.database"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

        setContentView(R.layout.activity_calander_base);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        mFunction = new UserFunctions(getApplicationContext());

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // Get the event
        mWeekView.getEventClickListener();

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);



        //FragmentManager for the calendar event
       /* final FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment == null){
            fragment = new Blank_Frag();
            //fragment = createdFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }*/

       /* button = (Button)findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getFragmentManager();
                Fragment f = fm.findFragmentById(R.id.fragmentContainer);

                if(f == null){
                    f = new EventActivity();
                   // fm.beginTransaction().add(R.id.fragmentContainer,f).commit();
                }

                android.app.FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.fragmentContainer, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/
    }

    @Override
    public void onClick(DialogInterface d, int i){

    }

    @Override
    public void onBackPressed(){
        if(fm.getBackStackEntryCount() != 0){
            fm.popBackStack();

        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override//ignore
    public boolean onPrepareOptionsMenu(Menu menu){
        if(fm.getBackStackEntryCount() == 0 || fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1) instanceof BasicActivity) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }else{
            getMenuInflater().inflate(R.menu.menu_frag, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            //Meneu option Today
            case R.id.action_today:
                mWeekView.goToToday();
                return true;

            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;

            //Menu option 3 days view
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;

            //Menu option week view
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;

            //Menu option logout
            case R.id.action_logout:
                item.setChecked(!item.isChecked());
                mFunction.logoutUser();
                Intent upanel = new Intent(getApplicationContext(), SignInActivity.class);
                upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(upanel);
                /**
                 * Close Main Screen
                 **/
                finish();
                return true;

            //Menu option setting
            case R.id.action_settings:
                return true;

            //Menu option add event
            case R.id.action_add_event:
                fm = getFragmentManager();
                Fragment f = fm.findFragmentById(R.id.fragmentContainer);

                if(f == null){//pass in event w/ defualt values
                    f = EventActivity.newInstance(new WeekViewEvent(3244, "", 2016, 1, 1, 0, 0, 2016, 1, 1, 0, 0));
                    // fm.beginTransaction().add(R.id.fragmentContainer,f).commit();
                }

                android.app.FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.fragmentContainer, f);
                ft.addToBackStack(null);
                ft.commit();
               // Intent addEvent = new Intent(getApplicationContext(),EventActivity.class);
               // addEvent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               // startActivity(addEvent);
               // return true;

          /*  case R.id.addButton:
                Intent adEvent = new Intent(getApplicationContext(),Blank_Frag.class);
                adEvent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(adEvent);
                return true;*/

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        clickid = event.getId();

        fm = getFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragmentContainer);

        if(f == null){
            f = EventActivity.newInstance(event);
            // fm.beginTransaction().add(R.id.fragmentContainer,f).commit();
        }

        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragmentContainer, f);
        ft.addToBackStack(null);
        ft.commit();


        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }
}
