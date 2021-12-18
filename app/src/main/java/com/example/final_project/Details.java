package com.example.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.Glide;


public class Details extends AppCompatActivity {

    private TextView title,explanation,date,link;
    private ImageView image,favourite;
    private NasaModel model;
    private SQLiteDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        init();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        model = (NasaModel) getIntent().getSerializableExtra("data");

        setData();

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isFavourite(String.valueOf(model.getId()))){
                    favourite.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.heart));
                    helper.removeFromFavourite(String.valueOf(model.getId()));
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.remove_from_favourite), Toast.LENGTH_SHORT).show();
                }else {
                    favourite.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.fav_on));
                    helper.addToFavourites(model);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.add_to_favourite), Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Details.this);
                builder.setMessage(getApplicationContext().getResources().getString(R.string.info_details));
                builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl();
            }
        });
    }

    //initializing java widget with xml views
    private void init(){
        title = findViewById(R.id.title);
        explanation = findViewById(R.id.explanation);
        date = findViewById(R.id.date);
        image = findViewById(R.id.image);
        favourite = findViewById(R.id.favourite);
        link = findViewById(R.id.link);
        helper = new SQLiteDatabaseHelper(getApplicationContext());
    }

    //setting data on xml layout
    private void setData(){
        title.setText(model.getTitle());
        explanation.setText(model.getExplanation());
        date.setText(Utils.formatDate(model.getDate()));
        link.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        link.setText(model.getHd_url());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);
        requestOptions.error(R.drawable.image_placeholder);
        Glide.with(getApplicationContext()).load(model.getUrl()).apply(requestOptions).into(image);


        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.lora);
        Typeface typeface2 = ResourcesCompat.getFont(getApplicationContext(),R.font.montserrat_bold);
        title.setTypeface(typeface2);
        explanation.setTypeface(typeface);

        if (helper.isFavourite(String.valueOf(model.getId()))){
            favourite.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.fav_on));
        }else {
            favourite.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.heart));
        }
    }

    //open hd url in browser
    private void openUrl(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(model.getHd_url()));
        startActivity(i);
    }
}