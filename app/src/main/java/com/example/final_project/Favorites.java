package com.example.final_project;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {

    private TextView no_favourites;
    private ImageView back;
    private ListView listView;
    private List<NasaModel> data;
    private SQLiteDatabaseHelper sqLiteDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        init();

        //when click on back image on xml onClick method runs and finish this activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //when click on info image on xml onClick method runs
        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( Favorites.this);
                builder.setMessage(getApplicationContext().getResources().getString(R.string.info_fav));
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
        no_favourites = findViewById(R.id.no_favourites);
        back = findViewById(R.id.back);
        listView = findViewById(R.id.listView);
        data = new ArrayList<>();
    }

    //load favourites data from SQLite Database
    private void loadData(){
        data.clear();
        Cursor cursor = sqLiteDatabaseHelper.getAllFavourites();
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

        if (data.size()>0){
            no_favourites.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }else{
            no_favourites.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        FavouriteAdapter adapter = new FavouriteAdapter( Favorites.this,data);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}