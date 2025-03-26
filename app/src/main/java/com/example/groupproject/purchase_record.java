package com.example.groupproject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.adapter.CartModel;
import com.example.groupproject.adapter.DrinkModel;
import com.example.groupproject.adapter.ICartLoadListener;
import com.example.groupproject.adapter.MyCartAdapter;
import com.example.groupproject.adapter.MyUpdateCartEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class purchase_record extends AppCompatActivity implements ICartLoadListener {
    ImageView mImageView;
    RelativeLayout mRelativeLayout;
    private TextView tv1,tv2;
    Button btn;
    ICartLoadListener mICartLoadListener;
    private DatabaseReference mDatabaseReference,mDatabaseReference2,mDatabaseReference3;
    RecyclerView mRecyclerView;
    private ICartLoadListener iCartLoadListener;
    private Context context;
    private List<DrinkModel> drinkModelList;

    public purchase_record(Context context, List<DrinkModel> drinkModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.drinkModelList = drinkModelList;
        this.iCartLoadListener = iCartLoadListener;
    }


    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_record);
        tv1 = findViewById(R.id.textView13);
        tv2 = findViewById(R.id.textView15);
        mImageView = findViewById(R.id.btnBack);
        mRecyclerView =findViewById(R.id.item);
        mRelativeLayout = findViewById(R.id.mainlayout);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        String sum = gv.getSum();
        tv2.setText(sum);
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseReference3 = FirebaseDatabase.getInstance().getReference("User")
                .child(mFirebaseUser.getUid())
                .child("purchase_record");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(mFirebaseUser.getUid())
                .child("balance");
        btn = findViewById(R.id.button7);
        mImageView.setOnClickListener(v -> finish());
        init();
        loadCartFromFirebase();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.getValue().toString();
                tv1.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText1 = tv1.getText().toString();
                String inputText2 = tv2.getText().toString();
                int num1=Integer.valueOf(inputText1).intValue();
                int num2=Integer.valueOf(inputText2).intValue();
                int num3 = num1-num2;
                if(num3>=0)
                    inputText1=String.valueOf(num3);
                    tv1.setText(inputText1);

                String money = tv1.getText().toString();
                HashMap hashMap = new HashMap();
                hashMap.put("balance",money);
                int finalNum = num1-num2;

                mDatabaseReference2.child(mFirebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        if(finalNum >=0)
                            Toast.makeText(purchase_record.this, "付款成功", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(purchase_record.this, "付款失敗, 餘額不足", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    private void loadCartFromFirebase(){
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(mFirebaseUser.getUid())
                .child("ConsumingRecords")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot cartSnapshot:snapshot.getChildren()){
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            mICartLoadListener.onCartLoadSuccess(cartModels);
                        }else{
                            mICartLoadListener.onCartLoadFailed("Record empty");
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mICartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });

    }
    private void init(){
        ButterKnife.bind(this);
        mICartLoadListener = (ICartLoadListener) this;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));

    }
    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        MyCartAdapter adapter = new MyCartAdapter(this,cartModelList);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mRelativeLayout,message,Snackbar.LENGTH_LONG).show();

    }
}
