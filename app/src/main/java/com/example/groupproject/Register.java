package com.example.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;


public class Register extends AppCompatActivity {
    Activity context = this;
    Button mbutton;
    TextView mTextView;
    EditText mEditView,mEditView1,mEditView2;
    RadioButton mRadioButton,mRadioButton1;
    CheckBox mCheckBox;
    ProgressBar mprogressBar;
    FirebaseAuth mAuth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mbutton = findViewById(R.id.Register);
        mEditView = findViewById(R.id.FUserName);
        mEditView1 = findViewById(R.id.Email);
        mEditView2 = findViewById(R.id.FPassword);
        mRadioButton = findViewById(R.id.radioButton3);
        mRadioButton1 = findViewById(R.id.radioButton4);
        mCheckBox = findViewById(R.id.FShow_Password);
        mprogressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        //註冊按鈕
        mbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String Username = mEditView.getText().toString().trim();
                String Email = mEditView1.getText().toString().trim();
                String Password = mEditView2.getText().toString().trim();
                String Sex = null;
                if (mRadioButton.isChecked()) {
                    Sex = "Male";
                } else if (mRadioButton1.isChecked()) {
                    Sex = "Female";
                }
                if (Username.isEmpty() || Email.isEmpty() || Password.isEmpty() || Sex == null) {
                    Toast.makeText(Register.this, "有少填東西", Toast.LENGTH_LONG).show();
                } else {
                    register(Username, Email, Password, Sex);
                }
            }
        });
        //CheckBox
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEditView2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEditView2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }
        //上傳至firebase
        private void register(final String Username, final String Email, final String Password, final String Sex){
            mprogressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser FirebaseUser = mAuth.getCurrentUser();
                        String UserID = mAuth.getUid();
                        userRef = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
                        HashMap<String, String> hashMap = new HashMap<>(0);
                        hashMap.put("UserId", UserID);
                        hashMap.put("Username", Username);
                        hashMap.put("Email", Email);
                        hashMap.put("Password", Password);
                        hashMap.put("Sex", Sex);
                        userRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mprogressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(Register.this,Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(Register.this,
                                            Objects.requireNonNull(task.getException().getMessage()),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else {
                        mprogressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this,
                                Objects.requireNonNull(task.getException().getMessage()),Toast.LENGTH_LONG).show();
                    }
                }
            });
    }
}