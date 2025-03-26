package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.groupproject.adapter.CartModel;
import com.example.groupproject.adapter.ICartLoadListener;
import com.example.groupproject.adapter.MyCartAdapter;
import com.example.groupproject.adapter.MyUpdateCartEvent;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class record extends AppCompatActivity implements ICartLoadListener {
    @BindView(R.id.purchas_record)
    RecyclerView mRecyclerView;
    @BindView(R.id.mainlayout)
    RelativeLayout mainlayout;
    ImageView mImageView;
    ICartLoadListener mICartLoadListener;

    DatabaseReference mDatabaseReference;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public  void onUpdataCart(MyUpdateCartEvent event){
        loadCartFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        firebaseInitial();
        init();
        loadCartFromFirebase();

        mImageView = findViewById(R.id.btnBack1);

        mImageView.setOnClickListener(v -> finish());


    }
    private void firebaseInitial(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(mFirebaseUser.getUid());
    }

    private void init(){
        ButterKnife.bind(this);

        mICartLoadListener = (ICartLoadListener) this;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));

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


    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {

        MyCartAdapter adapter = new MyCartAdapter(this,cartModelList);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainlayout,message,Snackbar.LENGTH_LONG).show();

    }
}