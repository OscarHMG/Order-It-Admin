package com.oscarhmg.orderit_server.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oscarhmg.orderit_server.Model.User;
import com.oscarhmg.orderit_server.R;
import com.oscarhmg.orderit_server.Utils.SessionManager;
import com.oscarhmg.orderit_server.Utils.UtilsDialog;
import com.oscarhmg.orderit_server.Utils.UtilsEditText;

public class SignIn extends AppCompatActivity {
    private final static String TAG = SignIn.class.getSimpleName();
    private EditText editTextPhone, editTextPassword;
    private Button signIn;
    private SessionManager session;

    private ProgressDialog progressDialog;

    private FirebaseDatabase db;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Init components

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        signIn = (Button) findViewById(R.id.signIn);
        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);

        //Init Firebase Reference
        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(editTextPhone.getText().toString(), editTextPassword.getText().toString());
            }
        });

    }

    private void doLogin(String phone, String password) {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get user information

                if (validateEditTexts()){
                    UtilsDialog.createAndShowDialogProgress(progressDialog,  getString(R.string.verify_login_info));

                    if(dataSnapshot.child(editTextPhone.getText().toString()).exists()) {
                        UtilsDialog.dismissDialog(progressDialog);
                        User user = dataSnapshot.child(editTextPhone.getText().toString()).getValue(User.class);
                        validateUserAdmin(user);
                    }else{
                        UtilsDialog.dismissDialog(progressDialog);
                        Toast.makeText(SignIn.this, getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    /**
     * Validate user, if all is correct, proceed to do login.
     * @param user
     */
    public void validateUserAdmin(User user){

        //User is registered?
        if(user.getPassword().equals(editTextPassword.getText().toString())) {

            //Is registered, but is member of the staff?
            if(!user.getIsStaffMember().equals("true")){
                Toast.makeText(this,  getString(R.string.isNotMemberStaff), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getString(R.string.login_succesfully), Toast.LENGTH_SHORT).show();
                session.createLoginSession(user.getName(), editTextPhone.getText().toString());

                Log.i(TAG, "Usuario: "+user.toString());
                Intent intentHome = new Intent(this, Home.class);
                startActivity(intentHome);
                finish();
            }
        }else
            Toast.makeText(this,  getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }

    /**
     * Validate editexts to avoid errors in the storage in DB
     * @return
     */
    public boolean validateEditTexts(){
        if(UtilsEditText.isEmpty(editTextPhone) || UtilsEditText.isEmpty(editTextPassword)){
            Toast.makeText(this, getString(R.string.field_empties), Toast.LENGTH_SHORT).show();
            return false;
        }else if(!UtilsEditText.isPhoneCorrectFormat(editTextPhone)){
            Toast.makeText(this, getString(R.string.field_phone_error), Toast.LENGTH_SHORT).show();
            return false;
        }else if(!UtilsEditText.isPasswordCorrectFormat(editTextPassword)){
            Toast.makeText(this, getString(R.string.field_password_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
