package com.oscarhmg.orderit_server.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oscarhmg.orderit_server.Interfaces.ItemClickListener;
import com.oscarhmg.orderit_server.Model.Food;
import com.oscarhmg.orderit_server.R;
import com.oscarhmg.orderit_server.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;

public class FoodActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference tableFood;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private RecyclerView recyclerViewFoodItems;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private String categoryId;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        //Get reference to the database and obtain the food table.
        database = FirebaseDatabase.getInstance();
        tableFood = database.getReference("Foods");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        initUIComponents();


    }


    public void initUIComponents(){
        recyclerViewFoodItems = (RecyclerView) findViewById(R.id.recyclerMenuFoodItems);
        recyclerViewFoodItems.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewFoodItems.setLayoutManager(layoutManager);

        //Add listener to the button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
            Log.i("OscarHMG","initent getting food..");
        }
        if(!categoryId.isEmpty() && categoryId !=null) {
            loadFoodByCategory();
            Log.i("OscarHMG","finish..");
        }

    }

    private void loadFoodByCategory() {
        Log.i("OscarHMG","Loading food..");
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                tableFood.orderByChild("MenuId").equalTo(categoryId)
                ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.getFoodName().setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.getImageFood());

                //Set click listener
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLonglick) {

                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerViewFoodItems.setAdapter(adapter);


    }


}
