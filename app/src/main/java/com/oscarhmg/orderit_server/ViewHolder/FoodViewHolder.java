package com.oscarhmg.orderit_server.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscarhmg.orderit_server.Interfaces.ItemClickListener;
import com.oscarhmg.orderit_server.R;

/**
 * Created by PC on 07/05/2018.
 */

public class  FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    private TextView foodName;
    private ImageView imageFood;
    private ItemClickListener itemClickListener;
    private ImageButton foodOptions;
    //private Activity activity;
    private Context context;


    public FoodViewHolder(View itemView, final Context context){
        super(itemView);
        this.context = context;

        foodName = (TextView) itemView.findViewById(R.id.food_title);
        imageFood = (ImageView) itemView.findViewById(R.id.food_image);
        foodOptions = (ImageButton) itemView.findViewById(R.id.btnFoodOptions);

        itemView.setOnClickListener(this);
        //foodOptions.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }

    public TextView getFoodName() {
        return foodName;
    }

    public void setFoodName(TextView foodName) {
        this.foodName = foodName;
    }

    public ImageView getImageFood() {
        return imageFood;
    }

    public void setImageFood(ImageView imageFood) {
        this.imageFood = imageFood;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ImageButton getFoodOptions() {
        return foodOptions;
    }

    public void setFoodOptions(ImageButton foodOptions) {
        this.foodOptions = foodOptions;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
