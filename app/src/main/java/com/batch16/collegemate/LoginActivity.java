package com.batch16.collegemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText uname,upass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get reference to both Email and Password Fields
        uname=findViewById(R.id.email);
        upass=findViewById(R.id.pass);

        //Firebase Authentication initialization
        mAuth= FirebaseAuth.getInstance();

    }
    public void InClicked(View view) {

        String em=uname.getText().toString();
        String pas= upass.getText().toString();
        //Self Explanatory Sign in with Email and Password
        mAuth.signInWithEmailAndPassword(em,pas).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If UserEmail and Password Matches -- Successful
                        if(task.isSuccessful()){
                            Intent in=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(in);
                        }else{
                            Toast.makeText(LoginActivity.this, "Please Enter Correct Info", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        //Checking if user already logged in
        if(user!=null){
            //String name=user.getDisplayName();
            String mail=user.getEmail();
            Toast.makeText(this, mail, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    public void resetpass(View view) {
        mAuth.sendPasswordResetEmail(uname.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Check your Mail to Complete the Process", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Try again after some Time", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}