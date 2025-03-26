package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class wallet extends AppCompatActivity {

    TextView mTextView,mTextView2;
    private DatabaseReference mDatabase;
    EditText edt1;
    Button btn;

    private DatabaseReference mDatabaseReference,mDatabaseReference2;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        mTextView = findViewById(R.id.textView12);
        edt1 = findViewById(R.id.editTextNumber);
        firebaseInitial();
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("Users");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(mFirebaseUser.getUid())
                .child("balance");

        btn = findViewById(R.id.button3);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.getValue().toString();
                mTextView.setText(data);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputText1 = edt1.getText().toString();
                String inputText2 = mTextView.getText().toString();
                int num1=Integer.valueOf(inputText1).intValue();
                int num2=Integer.valueOf(inputText2).intValue();
                num2 = num1 + num2;
                inputText2 = String.valueOf(num2);
                mTextView.setText(inputText2);

                String money = mTextView.getText().toString();
                HashMap hashMap = new HashMap();
                hashMap.put("balance",money);

                mDatabaseReference2.child(mFirebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(wallet.this, "加值成功",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void firebaseInitial(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(mFirebaseUser.getUid());
    }

}