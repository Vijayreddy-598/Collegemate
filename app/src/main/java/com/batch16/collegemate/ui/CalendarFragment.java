package com.batch16.collegemate.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.batch16.collegemate.Functions.MyDB;
import com.batch16.collegemate.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CalendarFragment extends Fragment {


    View root;
    private static final String TAG = "Test";
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private SimpleDateFormat dateFormatForYear = new SimpleDateFormat("yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForDate = new SimpleDateFormat("EEEE", Locale.getDefault());
    private CompactCalendarView compactCalendarView;

    MyDB my;
    Button addevent;
    int sdate,smonth,pos;
    ArrayAdapter adapter;
    //BottomSheet Var
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    List<String> mutableBookings;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_calender, container, false);
        mutableBookings = new ArrayList<>();
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
                addevent(root);
            }
        });


        //BottomList
        bottom_sheet = root.findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        //ListView Code
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mutableBookings);
        bookingsListView.setAdapter(adapter);

        bookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showBottomSheetDialog(position,sdate,smonth);
            }
        });

        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        //compactCalendarView.setIsRtl(false);
        //compactCalendarView.displayOtherMonthDays(true);
        //For Animation Open
        //compactCalendarView.showCalendarWithAnimation();
        //For Normal Open
        // compactCalendarView.showCalendar();
        Calendar calendar = Calendar.getInstance();
        loadEvents();
        //initial Date events
        Date presdate=calendar.getTime();
        sdate=presdate.getDate();
        smonth=presdate.getMonth();
        loaddayevents(smonth,sdate);

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

    public void loaddayevents(int month,int day){
        Calendar cal=Calendar.getInstance();
        cal.set(2020,month,day);
        Date newdate=cal.getTime();
        List<Event> bookingsFromMap = compactCalendarView.getEvents(newdate);
        if (bookingsFromMap != null) {
            Log.d(TAG, bookingsFromMap.toString());
            mutableBookings.clear();
            for (Event booking : bookingsFromMap) {
                mutableBookings.add((String) booking.getData());
            }
            adapter.notifyDataSetChanged();
        }
    }
    //Floating Action Button Add event Function
    private void addevent(View root) {

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
                //Add Event to SQLite
                String sevent=EventBox.getText().toString();
                ContentValues cv=new ContentValues();
                cv.put(my.E_COL_2,sdate);
                cv.put(my.E_COL_3,smonth);
                cv.put(my.E_COL_4,sevent);
                my.insertData(cv);
                Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
                //Add Event to present Calendar
                long timeInMillis = datetoMillis(2020,smonth,sdate);
                compactCalendarView.addEvent(new Event(R.color.colorAccent, timeInMillis, "Event is " + sevent));
                mutableBookings.add(sevent);
                /*loaddayevents(smonth,sdate);*/

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alertDialogBuilder.show();
    }


    private  void editevent(int day,int month,int pos,String event){
        Cursor c=my.getEventofDay(day,month);
        c.move(pos+1);
        int id=c.getInt(0);
        my.updateevent(id,event);
        adapter.notifyDataSetChanged();
    }

    private void loadEvents() {
           List<Event> events = getEvents();
           compactCalendarView.addEvents(events);
    }


    private List<Event> getEvents() {
        //Events through SQLdata
        List<Event> events=new ArrayList<>();
        Cursor c= my.readEventdata();
        while (c.moveToNext()){
            long timeInMillis = datetoMillis(2020,c.getInt(2),c.getInt(1));
            if(c.getString(3).equalsIgnoreCase("Wassup")){
                events.add(new Event(R.color.colorAccent, timeInMillis, "Event is " + c.getString(3)));
            }else{
                events.add(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event is " + c.getString(3)));
            }

        }
       return events;
    }


    // Convert Date into MilliSeconds, to add to correct Day.
    public long datetoMillis(int year,int month,int date) {
        Calendar calendar=Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(year, month, date);

        long timeInMillis = calendar.getTimeInMillis();

        return timeInMillis;
    }


    private void showBottomSheetDialog(final int pos,final int day,final int month){

        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.calender_events_list, null);

        LinearLayout edit=view.findViewById(R.id.lyt_edit);
        LinearLayout delete=view.findViewById(R.id.lyt_delete);
        LinearLayout copy=view.findViewById(R.id.lyt_copy);

       edit.setOnClickListener(new View.OnClickListener() {
           @Override public void onClick(View v) {
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
                        adapter.clear();
                        Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();

                        compactCalendarView.removeAllEvents();
                        loadEvents();
                        loaddayevents(month,day);
                        mBottomSheetDialog.cancel();
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
       delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(adapter.getItem(pos));
                Cursor c=my.getEventofDay(day,month);
                c.move(pos+1);
                int id=c.getInt(0);
                my.deleteonID(id);
                adapter.notifyDataSetChanged();

                compactCalendarView.removeAllEvents();
                loadEvents();
                loaddayevents(month,day);
                mBottomSheetDialog.cancel();

            }
       });
       copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.cancel();
            }
       });

        mBottomSheetDialog = new BottomSheetDialog(getContext());
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

}

