package com.oscarhmg.orderit_server.ViewHolder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscarhmg.orderit_server.Interfaces.ItemClickListener;
import com.oscarhmg.orderit_server.R;

/**
 * Created by OscarHMG on 02/12/2017.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView categoryName;
    private ImageView imageCategory;
    private ItemClickListener itemClickListener;


    public MenuViewHolder(View itemView) {
        super(itemView);

        categoryName = (TextView) itemView.findViewById(R.id.category_title);
        imageCategory = (ImageView) itemView.findViewById(R.id.category_image);
        itemView.setOnClickListener(this);


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
}
