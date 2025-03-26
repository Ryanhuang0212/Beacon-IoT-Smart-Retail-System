package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Window;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences .Editor editor;
    private CheckBox remember;
    Activity context = this;
    Button mbutton1,mbutton2;
    EditText mEditText,mEditText2;
    TextView mTextView;
    FirebaseAuth mAuth;
    String email;
    CheckBox mCheckBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTextView = findViewById(R.id.textView);
        mbutton1 = findViewById(R.id.button2);
        mbutton2 = findViewById(R.id.button5);
        mCheckBox = findViewById(R.id.checkBox);
        remember = findViewById(R.id.checkBox2);
        mEditText = findViewById(R.id.editTextTextPersonName);
        mEditText2 = findViewById(R.id.editTextTextPersonName2);
        boolean isRemember = preferences.getBoolean("remember_password",false);
        if (isRemember){
            String Name = preferences.getString("Name","");
            String Password = preferences.getString("Password","");
            mEditText.setText(Name);
            mEditText2.setText(Password);
            remember.setChecked(true);
        }
        mAuth = FirebaseAuth.getInstance();


        mbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEditText2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEditText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(mEditText.getText().toString(),mEditText2.getText().toString()).
                        addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                            String Name = mEditText.getText().toString();
                            String Password = mEditText2.getText().toString();
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            editor = preferences.edit();
                            if(remember.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("Name",Name);
                                editor.putString("Password",Password);
                            }else{editor.clear();}
                            editor.apply();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this, front_page.class);
                            startActivity(intent);
                            email = user.getEmail();
                            GlobalVariable gv = (GlobalVariable) getApplicationContext();
                            gv.setName(mEditText.getText().toString().trim());
                        }else{

                        }
                    }
                });
            }

        });
    }
}