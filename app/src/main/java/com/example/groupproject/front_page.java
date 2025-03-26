package com.example.groupproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class front_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CHANNEL_ID = "simplified_coding";
    private static final String CHANNEL_NAME = "Simplified Coding";
    private static final String CHANNEL_DECS = "Simplified Coding Notification";

    private final String TAG = front_page.class.getSimpleName();

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    private final static int REQUEST_ENABLE_BT = 1;
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;

    private BluetoothAdapter mBLEAdapter;
    TextView mBluetoothStatus,mTextView;
    private DatabaseReference mDatabaseReference;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Switch mSwitch;
    Button mButton;
    UserData mUserData;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;

    ImageView mImageView,mImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.front_page);
        GlobalVariable gv =(GlobalVariable) getApplicationContext();
        mImageView =(ImageView) findViewById(R.id.imageView5);
        mButton = findViewById(R.id.btn_notif);
        mSwitch = findViewById(R.id.switch1);
        mTextView = findViewById(R.id.textView3);
        mBLEAdapter = BluetoothAdapter.getDefaultAdapter();
        mImageView3 = findViewById(R.id.imageView2);
        firebaseInitial();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(mFirebaseUser.getUid())
                .child("give");
        mBluetoothStatus = findViewById(R.id.textView5);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(front_page.this,DrinkActivity.class);
                startActivity(intent);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification();
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bluetoothOn();
                }else{
                    bluetoothOFF();
                }
            }
        });

        mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(front_page.this,DrinkActivity.class);
                startActivity(intent);
            }
        });
        /*--------------------------Hooks------------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void bluetoothOFF() {
        mBLEAdapter.disable();
        mBluetoothStatus.setText("藍芽未開啟");
        Toast.makeText(getApplicationContext(),"Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }

    private void bluetoothOn() {
        if (!mBLEAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            mBluetoothStatus.setText("藍芽已開啟");
            Toast.makeText(getApplicationContext(),"Bluetooth turned on",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Bluetooth is already on", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayNotification(){
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                Picasso.get().load(link).into(mImageView3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void firebaseInitial(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(mFirebaseUser.getUid());
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;

            case R.id.nav_member:
                Intent intent = new Intent(front_page.this,member.class);
                startActivity(intent);
                break;

            case R.id.nav_bookkeeping:
                Intent intent1 = new Intent(front_page.this,Bookkeeping.class);
                startActivity(intent1);
                break;

            case R.id.nav_notion:
                Intent intent2 = new Intent(front_page.this, Notification.class);
                startActivity(intent2);
                break;
            case R.id.nav_search:
                Intent intent3 = new Intent(front_page.this,search.class);
                startActivity(intent3);
                break;
        }
        return false;
    }
}