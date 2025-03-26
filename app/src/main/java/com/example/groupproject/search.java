package com.example.groupproject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class search extends AppCompatActivity {
    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mResultList;
    private DatabaseReference mitemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mitemDatabase = FirebaseDatabase.getInstance().getReference("goods");

        mSearchField = (EditText) findViewById(R.id.Search);
        mSearchBtn = (ImageButton) findViewById(R.id.imageButton3);

        mResultList = (RecyclerView) findViewById(R.id.recyclerView);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mSearchField.getText().toString();
                firebaseItemSearch(searchText);
            }

            private void firebaseItemSearch(String searchText) {
                Toast.makeText(search.this,"Started Search",Toast.LENGTH_LONG).show();
                Query firebaseSearchQuery = mitemDatabase.orderByChild( "name" ).startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerAdapter<Itemdata, itemViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Itemdata, itemViewHolder>(
                                Itemdata.class,
                                R.layout.list_layout,
                                itemViewHolder.class,
                                firebaseSearchQuery
                        ) {
                            @Override
                            protected void populateViewHolder(itemViewHolder viewHolder, Itemdata model, int i) {
                                viewHolder.setDetails(getApplicationContext(),
                                        model.getName(), model.getPrice(), model.getImage());
                            }
                        };
                mResultList.setAdapter(firebaseRecyclerAdapter);
            }
        });
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context ctx, String name, String price, String image) {

            TextView item_name = (TextView) mView.findViewById(R.id.name);
            TextView item_prices = (TextView) mView.findViewById(R.id.price1);
            ImageView item_image = (ImageView) mView.findViewById(R.id.pic);

            item_name.setText(name);
            item_prices.setText(price);
            Glide.with(ctx).load(image).into(item_image);


        }


    }
}
