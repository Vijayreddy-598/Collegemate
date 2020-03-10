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


    MyDB my;
    static int count=0;
    public String passDayClicked;
    EditText date,month,event;
    Button addevent;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        root = inflater.inflate(R.layout.fragment_calender, container, false);
        final List<String> mutableBookings = new ArrayList<>();
        final ListView bookingsListView = root.findViewById(R.id.bookings_listview);
        final TextView moView=root.findViewById(R.id.samMonth);
        final TextView yearView=root.findViewById(R.id.samYear);
        final TextView dayView=root.findViewById(R.id.Day);
        final ImageView left=root.findViewById(R.id.leftArrow);
        final ImageView right=root.findViewById(R.id.rightArrow);

        my= new MyDB(getContext());

        date=root.findViewById(R.id.Date);
        month=root.findViewById(R.id.Month);
        event=root.findViewById(R.id.Event);
        addevent=root.findViewById(R.id.addevent);
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sevent=event.getText().toString();
                int sdate=Integer.parseInt(date.getText().toString());
                int smonth=Integer.parseInt(month.getText().toString());
                ContentValues cv=new ContentValues();
                cv.put(my.COL_1,sevent);
                cv.put(my.COL_2,sdate);
                cv.put(my.COL_3,smonth);
                cv.put(my.COL_4,70);
                my.insertData(cv);
                Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
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
           List<Event> events = getEvents(2);
           compactCalendarView.addEvents(events);
    }



    private List<Event> getEvents(int month) {
        //Events through SQLdata
        List<Event> events=new ArrayList<>();
        Cursor c= my.GetEventOn(month);
        while (c.moveToNext()){
            long timeInMillis = datetoMillis(2020,month,c.getInt(1));
            events.add(new Event(R.color.colorPrimary, timeInMillis, "Event is " + c.getString(0)));

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


    // Convert Date into MilliSeconds, to add to correct Day.
    public long datetoMillis(int year,int month,int date) {
        Calendar cal=Calendar.getInstance();
        setToMidnight(cal);
        cal.set(year, month, date);
        long timeInMillis = cal.getTimeInMillis();
        return timeInMillis;
    }

}

