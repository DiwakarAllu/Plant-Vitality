package com.example.plantvitality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    ImageView leafImg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //leafImg=findViewById(R.layout.fragment_home.id.leafImg);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        fab=findViewById(R.id.fab);

        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.nav_view);

        toolbar=(Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //toolbar.getBackground().setAlpha(0);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){
           navigationView.setCheckedItem(R.id.nav_home);
        }

        replaceFragment(new HomeFragment());

//        binding.bottomNavigationView.setBackground(null);
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.subscriptions:
                    Toast.makeText(this, "Chatbot", Toast.LENGTH_SHORT).show();
                    replaceFragment(new ChatFragment());
                    break;

            }

            return true;
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int id=item.getItemId();
//                if(id==R.id.nav_home){
//                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
//                } else if (id==R.id.nav_logout) {
//                    Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
//                } else if (id==R.id.nav_settings) {
//                    Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//                }
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), "Home panel is open", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(getApplicationContext(), "logout panel is open", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_about:
                        Toast.makeText(getApplicationContext(), "about panel is open", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_share:
                        Toast.makeText(getApplicationContext(), "share panel is open", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout imgLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout camLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout cropLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        imgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                //Toast.makeText(MainActivity.this,"Upload a Image is clicked",Toast.LENGTH_SHORT).show();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        camLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                //Toast.makeText(MainActivity.this,"Take a photo is Clicked",Toast.LENGTH_SHORT).show();
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        cropLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Your crops is Clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,YourCropsActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch (requestCode) {
//            case 0:
//                if (resultCode == RESULT_OK) {
//
//                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
//                    int dimension = Math.min(bitmap.getWidth(),bitmap.getHeight()); // to fit the screen
//                    bitmap= ThumbnailUtils.extractThumbnail(bitmap,dimension,dimension);// to fit the screen
//                    //leafImg.setImageBitmap(bitmap);
//                }
//                break;
//
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    Bitmap bitmap;
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    try { // don't forget its , getContext().getContentResolver() , in fragments ***.
//                        bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
//                        int dimension = Math.min(bitmap.getWidth(),bitmap.getHeight());
//                        bitmap= ThumbnailUtils.extractThumbnail(bitmap,dimension,dimension);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
////                   // for simple implementation but not at all good dimensional view ==>
////                    Uri selectedImage = imageReturnedIntent.getData();
////                    leafImg.setImageURI(selectedImage);
//                    final androidx.fragment.app
//                            .FragmentManager mFragmentManager
//                            = getSupportFragmentManager();
//                    final androidx.fragment.app
//                            .FragmentTransaction mFragmentTransaction
//                            = mFragmentManager.beginTransaction();
//                    final HomeFragment mFragment
//                            = new HomeFragment();
//
//                    Bundle mBundle = new Bundle();
//                    mBundle.putString("mText","bitmap");
//                    mFragment.setArguments(mBundle);
//                    mFragmentTransaction.add(R.id.fragment_container, mFragment).commit();
//                }
//                break;
//        }
//    }


}