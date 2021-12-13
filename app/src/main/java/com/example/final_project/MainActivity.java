package com.example.final_project;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
// import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView no_search_history;
    private ImageView menu,search_float;
    private ListView listView;
    private List<NasaModel> data;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SQLiteDatabaseHelper sqLiteDatabaseHelper;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        init();

        //when click on menu image on xml onClick method runs and open drawer from left side.
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //when you click on any item of navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.share:
                        share();
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchImage.class));
                        return true;
                    case R.id.favourites:
                        startActivity(new Intent(getApplicationContext(), Favorites.class));
                        return true;
                    case R.id.exit_app:
                        exitDialogue();
                        return true;
                    default:
                        return false;
                }
            }
        });

        //when click on search image on xml onClick method runs and move to search screen
        search_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchImage.class));
            }
        });

        //when click on info image on xml onClick method runs and show alert dialogue of information about screen
        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getApplicationContext().getResources().getString(R.string.info_main));
                builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    //initializing java widget with xml views
    private void init(){
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(getApplicationContext());
        search_float = findViewById(R.id.search_float);
        no_search_history = findViewById(R.id.no_search_history);
        navigationView = findViewById(R.id.navi);
        drawerLayout = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        listView = findViewById(R.id.listView);
        data = new ArrayList<>();
    }

    //load favourites data from SQLite Database
    private void loadData(){
        data.clear();
        Cursor cursor = sqLiteDatabaseHelper.getAllViewed();
        while (cursor.moveToNext()){
            NasaModel nasaModel = new NasaModel();
            nasaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.KEY_ID)));
            nasaModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.TITLE)));
            nasaModel.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.URL)));
            nasaModel.setHd_url(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.HD_URL)));
            nasaModel.setService_version(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.SERVICE_VERSION)));
            nasaModel.setMedia_type(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.MEDIA_TYPE)));
            nasaModel.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.EXPLANATION)));
            nasaModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteDatabaseHelper.DATE)));

            data.add(nasaModel);
        }
        cursor.close();
        if (data.size()>0) no_search_history.setVisibility(View.GONE); else no_search_history.setVisibility(View.VISIBLE);
        NewsAdapter adapter = new NewsAdapter(MainActivity.this,data);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    //share the app url via different ways
    private void share() {
        String share_str = getApplicationContext().getResources().getString(R.string.app_name)
                + " ("+getApplicationContext().getResources().getString(R.string.download_it_from_play_store)
                + " )\nhttp://play.google.com/store/apps/details?id="+getPackageName();
        Intent sharelink = new Intent(Intent.ACTION_SEND);
        sharelink.setType("text/*");
        sharelink.putExtra(Intent.EXTRA_SUBJECT,"");
        sharelink.putExtra(Intent.EXTRA_TEXT,share_str);
        startActivity(Intent.createChooser(sharelink,getApplicationContext().getResources().getString(R.string.share_via)));
    }

    //show exit dialogue and when user click yes then close the application
    private void exitDialogue(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage(getApplicationContext().getResources().getString(R.string.are_you_sure));
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}