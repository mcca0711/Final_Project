package com.example.final_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.example.final_project.SQLiteDatabaseHelper;
import com.example.final_project.SharedPreferenceManager;
import com.example.final_project.Details;
import com.example.final_project.NasaModel;
import com.example.final_project.R;
import com.example.final_project.Utils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public class SearchFragment extends Fragment {

    private TextView explanation,title;
    private ImageView image;
    private EditText date;
    private Calendar calendar;
    private Activity activity;
    private ProgressBar progress_circular;
    private RelativeLayout search_item;
    private SQLiteDatabaseHelper helper;
    private SharedPreferenceManager sharedPreferenceManager;
    private NasaModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        //when clicking info image the xml onClick method runs and shows the date picker
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                if (dayOfMonth<10 && (monthOfYear + 1)<10){
                                    date.setText(year + "-0"+ (monthOfYear + 1) + "-0" + dayOfMonth);
                                }else if (10 > (monthOfYear + 1)){
                                    date.setText(year + "-0"+ (monthOfYear + 1) + "-" + dayOfMonth);
                                }else if (dayOfMonth < 10){
                                    date.setText(year + "-"+ (monthOfYear + 1) + "-0" + dayOfMonth);
                                }else {
                                    date.setText(year + "-"+ (monthOfYear + 1) + "-" + dayOfMonth);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        //when clicking search button the xml onClick method runs and fetches data from api
        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.getText().toString().isEmpty()){
                    Snackbar.make(view,activity.getResources().getString(R.string.enter_date_for_search),Snackbar.LENGTH_LONG).show();
                    return;
                }
                FetchDataAsyncTask fetchDataAsyncTask = new FetchDataAsyncTask();
                fetchDataAsyncTask.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date="+date.getText().toString().trim());
            }
        });

        //when you click on searched data this methods runs and moves to details screen
        search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Details.class);
                intent.putExtra("data",model);
                startActivity(intent);
            }
        });
    }

    //initializing java widget with xml views
    private void init(View view){
        title = view.findViewById(R.id.title);
        progress_circular = view.findViewById(R.id.progress_circular);
        search_item = view.findViewById(R.id.search_item);
        image = view.findViewById(R.id.image);
        date = view.findViewById(R.id.date);
        explanation = view.findViewById(R.id.explanation);

        calendar = Calendar.getInstance();
        helper = new SQLiteDatabaseHelper(activity);
        sharedPreferenceManager = new SharedPreferenceManager(activity);

        date.setText(sharedPreferenceManager.getDate());
    }

    //fetching data from API using AsyncTask and set load data on screen
    private class FetchDataAsyncTask extends AsyncTask<String, Integer, NasaModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_circular.setVisibility(View.VISIBLE);
            search_item.setVisibility(View.GONE);
            Snackbar.make(getView(),activity.getResources().getString(R.string.please_wait),Snackbar.LENGTH_SHORT).show();

        }
        @Override
        protected NasaModel doInBackground(String... strings) {
            NasaModel model = new NasaModel();
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder dta = new StringBuilder();
                String chunks;
                while ((chunks = buff.readLine()) != null) {
                    dta.append(chunks);
                }
                String nasa_data = dta.toString();
                JSONObject object = new JSONObject(nasa_data);

                model.setDate(object.getString("date"));
                model.setExplanation(object.getString("explanation"));
                model.setHd_url(object.getString("hdurl"));
                model.setMedia_type(object.getString("media_type"));
                model.setService_version(object.getString("service_version"));
                model.setTitle(object.getString("title"));
                model.setUrl(object.getString("url"));
                return model;
            }catch (Exception e){
                Log.d("TAG", "doInBackground: "+e.getMessage());
                Snackbar.make(getView(),activity.getResources().getString(R.string.something_went_wrong),Snackbar.LENGTH_LONG).show();
                return model;
            }
        }

        @Override
        protected void onPostExecute(NasaModel nasaModel) {
            super.onPostExecute(nasaModel);
            model = nasaModel;
            progress_circular.setVisibility(View.GONE);
            search_item.setVisibility(View.VISIBLE);
            title.setText(nasaModel.getTitle());
            explanation.setText(nasaModel.getExplanation());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);
            requestOptions.error(R.drawable.image_placeholder);
            Glide.with(activity).load(nasaModel.getUrl()).apply(requestOptions).into(image);

            helper.addToViewed(nasaModel);
            sharedPreferenceManager.setDate(date.getText().toString().toString());
        }

    }
}
