package com.oscarhmg.orderit_server.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.oscarhmg.orderit_server.R;


/**
 * Created by OscarHMG on 26/11/2017.
 */

public class UtilsDialog {

    public static void createAndShowDialogProgress(ProgressDialog progressDialog, String message){
        //progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public static void dismissDialog(ProgressDialog progressDialog){
        progressDialog.dismiss();
    }


    public static void showDialogToCreateCategoryFood(Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getString(R.string.createCategoryTitle));
        alertDialog.setMessage("");

        LayoutInflater inflater = activity.getLayoutInflater();
        View layoutNewCategory = inflater.inflate(R.layout.dialog_upload_category_layout, null);

    }
}
