package com.example.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.final_project.SearchFragment;
import com.example.final_project.NasaModel;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SearchImage extends AppCompatActivity {

    private FrameLayout container;
    private ImageView back;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);

        init();

        //when clicking on the back image the xml onClick method runs and finishes this activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadFragment();

        //when clicking on info image the xml onClick method runs and shows alert dialogue of information about screen
        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchImage.this);
                builder.setMessage(getApplicationContext().getResources().getString(R.string.info_search));
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
        container = findViewById(R.id.container);
        back = findViewById(R.id.back);
    }

    //loading fragment into frame layout
    private void loadFragment(){
        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.container, new SearchFragment(), "Search Fragment")
                .disallowAddToBackStack()
                .commit();
    }

}