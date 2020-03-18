package com.batch16.collegemate.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import com.batch16.collegemate.Functions.MyDB;
import com.batch16.collegemate.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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
    ListView listView;

    MyDB my;
    static int count=0;
    Button addevent,deleteevent,editevent;
    int sdate,smonth,pos;
   ArrayAdapter adapter;


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

        bookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
            }
        });


        //AddEvent by Button
        addevent=root.findViewById(R.id.addevent);
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(root);
            }
        });



        deleteevent=root.findViewById(R.id.deleteevent);
        deleteevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(sdate,smonth,pos);
            }
        });



        editevent=root.findViewById(R.id.editevent);
        editevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                Context context=root.getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(24,24,24,24);

                TextView tv=new TextView(context);
                tv.setText("Edit Event name on "+sdate);
                tv.setTextSize(24);
                layout.addView(tv);

                final EditText EventBox = new EditText(context);
                EventBox.setHint("Enter Event");
                EventBox.setInputType(InputType.TYPE_CLASS_TEXT);
                layout.addView(EventBox);

                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        String sevent=EventBox.getText().toString();
                        editevent(sdate,smonth,pos,sevent);
                        Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();

                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alertDialogBuilder.show();
            }


        });


        //ListView Code
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mutableBookings);
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
                        sdate =dateClicked.getDate();
                        smonth =dateClicked.getMonth();
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


    //Floating Action Button Add event Function
    private void open(View root) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        Context context=root.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24,24,24,24);

        TextView tv=new TextView(context);
        tv.setText("Enter Event name on "+sdate);
        tv.setTextSize(24);
        layout.addView(tv);

        final EditText EventBox = new EditText(context);
        EventBox.setHint("Enter Event");
        EventBox.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(EventBox);

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String sevent=EventBox.getText().toString();
                ContentValues cv=new ContentValues();
                cv.put(my.COL_2,sdate);
                cv.put(my.COL_3,smonth);
                cv.put(my.COL_4,sevent);
                my.insertData(cv);
                Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alertDialogBuilder.show();
    }


    private  void deleteEvent(int day,int month,int pos){
        adapter.remove(adapter.getItem(pos));
        Cursor c=my.getEventofDay(day,month);
        c.move(pos+1);
        int id=c.getInt(0);
        my.deleteonID(id);
    }
    private  void editevent(int day,int month,int pos,String event){
        Cursor c=my.getEventofDay(day,month);
        c.move(pos+1);
        int id=c.getInt(0);
        my.editSelected(id,event);
        adapter.notifyDataSetChanged();
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
            events.add(new Event(R.color.colorPrimary, timeInMillis, "Event is " + c.getString(3)));

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

