package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class member extends AppCompatActivity   {
    UserData mUserData;
    String username;
    TextView mTextView;
    Button mButton,mButton2,mButton3;
    DatabaseReference myRef;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        mTextView = findViewById(R.id.textView4);
        mButton = findViewById(R.id.button8);
        mButton2 = findViewById(R.id.button9);
        mButton3 = findViewById(R.id.button6);
        GlobalVariable gv =(GlobalVariable) getApplicationContext();

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(member.this,record.class);
                startActivity(intent);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(member.this,qrcode.class);
                startActivity(intent);
            }


        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(member.this,wallet.class);
                startActivity(intent);
            }


        });
        firebaseInitial();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUserData = snapshot.getValue(UserData.class);
                if (mUserData != null){
                    username = mUserData.getUsername();
                }
                if(username != null){
                    mTextView.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void firebaseInitial(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseUser.getUid());
    }
}