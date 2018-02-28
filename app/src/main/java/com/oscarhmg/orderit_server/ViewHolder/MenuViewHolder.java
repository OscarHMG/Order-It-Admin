package com.oscarhmg.orderit_server.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscarhmg.orderit_server.Interfaces.ItemClickListener;
import com.oscarhmg.orderit_server.R;

import java.util.logging.Logger;

/**
 * Created by OscarHMG on 02/12/2017.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener{
    private TextView categoryName;
    private ImageView imageCategory;
    private ItemClickListener itemClickListener;
    private ImageButton btnOptions;
    private Activity activity;

    public MenuViewHolder(View itemView, final Activity activity) {
        super(itemView);
        this.activity = activity;
        categoryName = (TextView) itemView.findViewById(R.id.category_title);
        imageCategory = (ImageView) itemView.findViewById(R.id.category_image);
        btnOptions = (ImageButton) itemView.findViewById(R.id.btnOptions);

        /*btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("OSCARHMG","Click!");
                view.setOnCreateContextMenuListener(MenuViewHolder.this);
                activity.registerForContextMenu(view);
                activity.openContextMenu(view);
            }
        });*/

        itemView.setOnClickListener(this);
        btnOptions.setOnCreateContextMenuListener(this);


    }




    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public TextView getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(TextView categoryName) {
        this.categoryName = categoryName;
    }

    public ImageView getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(ImageView imageCategory) {
        this.imageCategory = imageCategory;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ImageButton getBtnOptions() {
        return btnOptions;
    }

    public void setBtnOptions(ImageButton btnOptions) {
        this.btnOptions = btnOptions;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle(activity.getApplicationContext().getString(R.string.titleContextMenu));
        contextMenu.add(0,0,getAdapterPosition(), activity.getApplicationContext().getString(R.string.updateLabel));
        contextMenu.add(0,1,getAdapterPosition(),activity.getApplicationContext().getString(R.string.deleteLabel));
    }
}
