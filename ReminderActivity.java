package com.example.springroll.database;

import android.app.Fragment;

/**
 * Created by SpringRoll on 4/30/2016.
 */
public class ReminderActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createdFragment(){
        long eventId = (long)getIntent().getSerializableExtra(ReminderFragment.REMINDER_EVENT_ID);
        return ReminderFragment.newInstance(eventId);
    }
}
