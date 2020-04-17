package com.batch16.collegemate.Functions;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import com.batch16.collegemate.R;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(true);
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH);

        addSlide(new SimpleSlide.Builder()
                .title("Welcome")
                .description("The entire team of Collegemate is thrilled to welcome you on board. We hope you’ll do some amazing works here!")
                .image(R.drawable.intro_0_book_lover)
                .background(R.color.white)
                .backgroundDark(R.color.colorPrimary)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("College Notifications")
                .description("All the fresh trimmed news just for you")
                .image(R.drawable.intro_1_admin_notif)
                .background(R.color.white)
                .backgroundDark(R.color.colorPrimary)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Smart Silent Mode")
                .description("From now on we will keep the task of setting your device silent when you are around College")
                .image(R.drawable.intro_2_silentmode)
                .background(R.color.white)
                .backgroundDark(R.color.colorPrimary)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Nearby Friends")
                .description("We will let you know when friends are closeby \n \" No Place is bad when you have Friends around \"- Random Guy on Internet \n Grant location \"Allow all the time\" permissions \nfor smooth experience  ")
                .image(R.drawable.intro_3_nearbyfriends)
                .background(R.color.white)
                .backgroundDark(R.color.colorPrimary)
                .permissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Customized Calender")
                .description("Get all the information from one place.Your day-to-day attendance,College Events,Birthdays")
                .image(R.drawable.intro_4_calender)
                .background(R.color.white)
                .backgroundDark(R.color.colorPrimary)
                .buttonCtaLabel("Get Started")
                .build());
    }
}
