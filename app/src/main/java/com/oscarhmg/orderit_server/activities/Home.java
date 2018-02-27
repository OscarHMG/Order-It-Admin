package com.oscarhmg.orderit_server.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oscarhmg.orderit_server.Interfaces.ItemClickListener;
import com.oscarhmg.orderit_server.Model.Category;
import com.oscarhmg.orderit_server.R;
import com.oscarhmg.orderit_server.Utils.SessionManager;
import com.oscarhmg.orderit_server.Utils.UtilsDialog;
import com.oscarhmg.orderit_server.ViewHolder.MenuViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SessionManager session;
    private FirebaseDatabase database;
    private DatabaseReference tableCategory;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private TextView userNameLogged;
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    private MaterialEditText newCategoryName;
    private FButton btnSelectCategoryImage, btnUploadCategory;

    private Category newCategory;

    private final static int PICK_IMAGE_REQUEST = 9990;

    private Uri saveURI;

    private ProgressDialog progressDialogNewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.adminMenuTitle));
        setSupportActionBar(toolbar);

        //Handle Session and connect with Firebase
        session = new SessionManager(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        tableCategory = database.getReference("Category");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UtilsDialog.showDialogToCreateCategoryFood(Home.this);
                showCreateCategoryDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set info of the admin logged

        recyclerMenu = (RecyclerView) findViewById(R.id.recyclerMenu);
        recyclerMenu.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerMenu.setLayoutManager(layoutManager);

        View header = navigationView.getHeaderView(0);
        userNameLogged = (TextView) header.findViewById(R.id.userLoggedName);
        userNameLogged.setText(session.getUserDetails().get(SessionManager.KEY_NAME));


        loadCategoryFood();
    }



    private void loadCategoryFood() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,
                R.layout.category_item,
                MenuViewHolder.class,
                tableCategory
                ) {

            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // create a new view
                View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
                //RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //itemLayoutView.setLayoutParams(lp);
                // create ViewHolder
                MenuViewHolder viewHolder = new MenuViewHolder(itemLayoutView);
                return viewHolder;
            }

            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                //Here thw view is setted with the info.
                viewHolder.getCategoryName().setText(model.getName());

                //Now download image.
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.getImageCategory());

                //final Category item = model;
                //Implement own listener

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLonglick) {

                        /*Intent foodByCategory = new Intent(Home.this, FoodMenu.class);
                        foodByCategory.putExtra("idCategory", adapter.getRef(position).getKey());
                        startActivity(foodByCategory);*/
                    }
                });

            }


        };

        recyclerMenu.setAdapter(adapter);
    }


    private void showCreateCategoryDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle(getString(R.string.createCategoryTitle));
        alertDialog.setMessage("");

        LayoutInflater inflater = this.getLayoutInflater();
        View layoutNewCategory = inflater.inflate(R.layout.dialog_upload_category_layout, null);

        newCategoryName = (MaterialEditText) layoutNewCategory.findViewById(R.id.newCategoryName);
        btnSelectCategoryImage = (FButton) layoutNewCategory.findViewById(R.id.btnUploadPicture);
        btnUploadCategory = (FButton) layoutNewCategory.findViewById(R.id.btnCreateCategory);
        btnUploadCategory.setEnabled(false);
        btnSelectCategoryImage.setEnabled(false);
        //Actions of Buttons


        alertDialog.setView(layoutNewCategory);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        final AlertDialog dialog = alertDialog.show();
        setButtonListenerActions(dialog);
        //alertDialog.show();

    }

    private void setButtonListenerActions(final AlertDialog alertDialog) {
        newCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 5){
                    btnSelectCategoryImage.setEnabled(true);
                }else{
                    Toast.makeText(Home.this, getString(R.string.createCategorySErrorName), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromPhone();
                //alertDialog.dismiss();
            }
        });

        btnUploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNewImageCategory();
            }
        });
    }



    private void chooseImageFromPhone() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.createCategoryIntent)), PICK_IMAGE_REQUEST);
    }

    private void uploadNewImageCategory() {
        if(saveURI != null){
            progressDialogNewCategory = new ProgressDialog(this);
            UtilsDialog.createAndShowDialogProgress(progressDialogNewCategory, getString(R.string.createCategoryLoading));

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialogNewCategory.dismiss();
                    Toast.makeText(Home.this, getString(R.string.createCategorySuccesful), Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newCategory = new Category(newCategoryName.getText().toString(), uri.toString());
                            //Here create category in Firebase
                            tableCategory.push().setValue(newCategory);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialogNewCategory.dismiss();
                    Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()));
                    progressDialogNewCategory.setMessage("Creando "+progress+"%");
                }
            });
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data!=null && data.getData()!=null){
            saveURI = data.getData();
            btnUploadCategory.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
