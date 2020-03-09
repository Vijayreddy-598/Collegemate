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
        uname=findViewById(R.id.email);
        upass=findViewById(R.id.pass);

        mAuth= FirebaseAuth.getInstance();

    }
    public void InClicked(View view) {
        String em=uname.getText().toString();
        String pas= upass.getText().toString();
        mAuth.signInWithEmailAndPassword(em,pas)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
        if(user!=null){
            String name=user.getDisplayName();
            String mail=user.getEmail();
            Toast.makeText(this, name+"\n"+mail, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }

    public void resetpass(View view) {
        mAuth.sendPasswordResetEmail(uname.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Check Yo Mail Homie", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Stop Playing Fool", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}