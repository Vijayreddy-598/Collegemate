package com.batch16.collegemate.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.batch16.collegemate.Functions.MyDB;
import com.batch16.collegemate.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Boolean.TRUE;


public class CalendarFragment extends Fragment {
    View root;
    private static final String TAG = "Babu";
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private SimpleDateFormat dateFormatForYear = new SimpleDateFormat("yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForDate = new SimpleDateFormat("EEEE", Locale.getDefault());
    private boolean shouldShow = false;
    private CompactCalendarView compactCalendarView;
    private ActionBar toolbar;
    MyDB my;
    static int count=0;
    public String passDayClicked;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_calender, container, false);
        final List<String> mutableBookings = new ArrayList<>();
        final ListView bookingsListView = root.findViewById(R.id.bookings_listview);
        final TextView moView=root.findViewById(R.id.Month);
        final TextView yearView=root.findViewById(R.id.Year);
        final TextView dayView=root.findViewById(R.id.Day);
        final ImageView left=root.findViewById(R.id.leftArrow);
        final ImageView right=root.findViewById(R.id.rightArrow);


        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open(root);
            }
        });


        final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mutableBookings);
        bookingsListView.setAdapter(adapter);
        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        //compactCalendarView.setIsRtl(false);
        //compactCalendarView.displayOtherMonthDays(false);
        //For Animation Open
        //compactCalendarView.showCalendarWithAnimation();
        //For Noramal Open
        // compactCalendarView.showCalendar();
        Calendar calendar = Calendar.getInstance();
        loadEvents();
        yearView.setText(dateFormatForYear.format(calendar.getTime()));
        dayView.setText(dateFormatForDate.format(calendar.getTime()));
        moView.setText(dateFormatForMonth.format(calendar.getTime()));
        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                moView.setText(dateFormatForMonth.format(dateClicked));
                yearView.setText(dateFormatForYear.format(dateClicked));
                dayView.setText(dateFormatForDate.format(dateClicked));
                Log.d(TAG, "inside onclick " + dateFormatForDisplaying.format(dateClicked));
                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);

                if (bookingsFromMap != null) {
                    Log.d(TAG, bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add((String) booking.getData());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                moView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                yearView.setText(dateFormatForYear.format(firstDayOfNewMonth));
                dayView.setText(dateFormatForDate.format(firstDayOfNewMonth));
            }
        });

        //Scroll Left Right Fuctionality
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });

        return root;
    }

    private void loadEvents() {
/*        addEvents(-1, -1);
       addEvents(Calendar.DECEMBER, -1);
       addEvents(Calendar.AUGUST, -1);*/
        Log.i(TAG, "loadEvents: Called");
       Date firstDayOfMonth = currentCalender.getTime();
       firstDayOfMonth.setDate(1);
       firstDayOfMonth.setMonth(0);
       for (int i = 0; i < 6; i++) {
          /* currentCalender.setTime(firstDayOfMonth);
           currentCalender.add(Calendar.DATE,i);
           setToMidnight(currentCalender);
           long timeInMillis = currentCalender.getTimeInMillis();*/
           List<Event> events = getEvents(i);
           compactCalendarView.addEvents(events);
       }
    }
    private List<Event> getEvents(int month) {
        //Events through SQLdata
        List<Event> events=new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            long timeInMillis = datetoMillis(2020,month,i);
            events.add(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Absent From at " + new Date(timeInMillis)));
            events.add(new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
        }
       return events;
    }
    private void addEvents(int month, int year) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
    }
    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void open(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        final EditText edittext = new EditText(getContext());
        alertDialogBuilder.setView(edittext);
        alertDialogBuilder.setTitle("Enter Your title");
        alertDialogBuilder.setMessage("Enter message: ");
        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String YouEditTextValue = edittext.getText().toString();
                long dateMillis = datetoMillis(2020,2,14);
                Log.d(TAG, "onClick: DATE: + " + dateMillis);
                Event ev1 = new Event(Color.GREEN, dateMillis, YouEditTextValue);
                compactCalendarView.addEvent(ev1);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alertDialogBuilder.show();
    }
    // Convert Date into MilliSeconds, to add to correct Day.
    public long datetoMillis(int year,int month,int date) {
        Calendar cal=Calendar.getInstance();
        setToMidnight(cal);
        cal.set(year, month, date);
        long timeInMillis = cal.getTimeInMillis();
        return timeInMillis;
    }

}

